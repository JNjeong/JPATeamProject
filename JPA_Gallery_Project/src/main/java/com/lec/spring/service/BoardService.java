package com.lec.spring.service;

import com.lec.spring.domain.Board;
import com.lec.spring.domain.FileDTO;
import com.lec.spring.domain.User;
import com.lec.spring.repository.BoardRepository;
import com.lec.spring.repository.FileRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.Util;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BoardService {

    @Value("${app.upload.path}")
    private String uploadDir;

    private BoardRepository boardRepository;
    private UserRepository userRepository;

    private FileRepository fileRepository;

    @Autowired
    public void setWriteRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public BoardService(){
        System.out.println("BoardService() 생성");
    }

    public int write(Board board, Map<String, MultipartFile> files){
        User user = Util.getLoggedUser();
        user = userRepository.findById(user.getId()).orElse(null);

        board.setUser(user);
        board = boardRepository.saveAndFlush(board);

        addFiles(files, board.getId());
        return 1;
    }

    private void addFiles(Map<String, MultipartFile> files, Long id){
        if(files != null){
            for(Map.Entry<String, MultipartFile> e :files.entrySet()){
                if(!e.getKey().startsWith("upfile")) continue;

                System.out.println("\n첨부파일 정보: " + e.getKey());
                Util.printFileInfo(e.getValue());
                System.out.println();
                FileDTO file = upload(e.getValue());

                if(file != null){
                    file.setBoard(id);
                    fileRepository.save(file);
                }
            }
        }
    }

    private FileDTO upload(MultipartFile multipartFile){
        FileDTO attachment = null;

        String originalFilename = multipartFile.getOriginalFilename();
        if(originalFilename == null || originalFilename.length() == 0) return null;

        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileName = sourceName;

        File file = new File(uploadDir + File.separator + sourceName);
        if(file.exists()){
            int pos = fileName.lastIndexOf(".");
            if(pos > -1) {
                String name = fileName.substring(0, pos);
                String ext = fileName.substring(pos + 1);
                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            } else {
                fileName += "_" + System.currentTimeMillis();
            }
        }
        Path copyOfLocation = Paths.get(new File(uploadDir + File.separator + fileName).getAbsolutePath());
        System.out.println(copyOfLocation);
        try {
            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        attachment = FileDTO.builder().file(fileName).source(sourceName).build();

        return attachment;
    }


    @Transactional
    public List<Board> detail(long id) {
        List<Board> list = new ArrayList<>();
        Board board = boardRepository.findById(id).orElse(null);

        if(board != null){
            board.setViewCnt(board.getViewCnt() + 1);
            boardRepository.saveAndFlush(board);
            List<FileDTO> fileList = fileRepository.findByWrite(board.getId());
            setImage(fileList);
            board.setFileList(fileList);
            list.add(board);
        }
        return list;
    }

    private void setImage(List<FileDTO> fileList) {
        String realPath = new File(uploadDir).getAbsolutePath();

        for(FileDTO fileDto : fileList) {
            BufferedImage imgData = null;
            File f = new File(realPath, fileDto.getFile());
            try {
                imgData = ImageIO.read(f);
            }
            catch (IOException e) {
                System.out.println("파일존재안함: " + f.getAbsolutePath() + " [" + e.getMessage() + "]");
            }
            if(imgData != null) fileDto.setImage(true);
        }
    }

    public List<Board> list(){
        return boardRepository.findAll();
    }

    public List<Board> selectById(long id) {
        List<Board> list = new ArrayList<>();
        Board write = boardRepository.findById(id).orElse(null);

        if(write != null){
            List<FileDTO> fileList = fileRepository.findByWrite(write.getId());
            setImage(fileList);
            write.setFileList(fileList);
            list.add(write);
        }
        return list;
    }

    public List<Board> list(Integer page, Model model){
        if(page == null) page = 1;
        if(page < 1) page = 1;

        HttpSession session = Util.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = 10;
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = 10;
        session.setAttribute("page", page);
        Page<Board> pageWrites = boardRepository.findAll(PageRequest.of(page-1, pageRows, Sort.by(Sort.Order.desc("id"))));

        long cnt = pageWrites.getTotalElements();
        int totalPage =  pageWrites.getTotalPages();
        if(page > totalPage) page = totalPage;
        int startPage = ((int)((page - 1) / writePages) * writePages) + 1;
        int endPage = startPage + writePages - 1;
        if (endPage >= totalPage) endPage = totalPage;

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수
        model.addAttribute("url", Util.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        List<Board> list = pageWrites.getContent();
        model.addAttribute("list", list);

        return list;
    }



    public int update(Board write, Map<String, MultipartFile> files, Long[] delfile){
        int result = 0;

        Board w =boardRepository.findById(write.getId()).orElse(null);
        if(w != null){
            w.setSubject(write.getSubject());
            w.setContent(write.getContent());
            boardRepository.save(w);
            addFiles(files, write.getId());
            if(delfile != null){
                for(Long fileId : delfile){
                    FileDTO file = fileRepository.findById(fileId).orElse(null);
                    if(file != null){
                        delFile(file);
                        fileRepository.delete(file);
                    }
                }
            }
            return 1;
        }
        return result;
    }

    public int deleteById(long id){
        int result = 0;
        Board board = boardRepository.findById(id).orElse(null);
        if(board != null) {
            List<FileDTO> fileList = fileRepository.findByWrite(id);
            if(fileList != null && fileList.size() > 0) {
                for(FileDTO file : fileList) {delFile(file);}
                boardRepository.delete(board);
                return 1;
            }
        }
        return result;
    }

    private void delFile(FileDTO file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();
        File f = new File(saveDirectory, file.getFile());
        System.out.println("삭제시도--> " + f.getAbsolutePath());

        if (f.exists()) {
            if (f.delete()) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

}

