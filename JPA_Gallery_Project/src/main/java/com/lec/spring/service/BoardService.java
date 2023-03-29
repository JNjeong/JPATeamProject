package com.lec.spring.service;

import com.lec.spring.common.C;
import com.lec.spring.domain.Board;
import com.lec.spring.domain.FileDTO;
import com.lec.spring.domain.User;
import com.lec.spring.domain.Write;
import com.lec.spring.repository.BoardRepository;
import com.lec.spring.repository.FileRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.repository.WriteRepository;
import com.lec.spring.util.U;
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

    public int write(Board board
            , Map<String, MultipartFile> files   // 첨부 파일들.
    ){
        // 현재 로그인한 작성자 정보
        User user = Util.getLoggedUser();

        // 위 정보는 session 의 정보이고, 일단 DB 에서 다시 읽어온다
        user = userRepository.findById(user.getId()).orElse(null);
        board.setUser(user);  // 글 작성자 세팅

        board = writeRepository.saveAndFlush(board);

        // 첨부파일 추가
        addFiles(files, board.getId());

        return 1;
    }

    // 특정 글(id) 첨부파일(들) 추가
    private void addFiles(Map<String, MultipartFile> files, Long id){
        if(files != null){
            for(Map.Entry<String, MultipartFile> e :files.entrySet()){

                // name="upfile##" 인 경우만 첨부파일 등록. (이유, 다른 웹에디터와 섞이지 않도록..ex: summernote)
                if(!e.getKey().startsWith("upfile")) continue;

                // 첨부파일 정보 출력
                System.out.println("\n첨부파일 정보: " + e.getKey());   // name값
                Util.printFileInfo(e.getValue());
                System.out.println();

                // 물리적인 파일 저장
                File file = upload(e.getValue());

                // 성공하면 DB 에도 저장
                if(file != null){
                    file.setBoard(id);   // FK 설정
                    fileRepository.save(file);   // INSERT
                }
            }
        }
    }// end addFiles()

    // 물리적으로 파일 저장.  중복된 이름 rename 처리
    private File upload(MultipartFile multipartFile){
        File attachment = null;

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

        attachment = File.builder()
                .file(fileName)   // 저장된 이름
                .source(sourceName)  // 원본이름
                .build();

        return attachment;
    } // end upload


    @Transactional
    public List<Board> detail(long id) {
        List<Board> list = new ArrayList<>();

        Board write = writeRepository.findById(id).orElse(null);

        if(write != null){
            // 조회수 증가
            write.setViewCnt(write.getViewCnt() + 1);
            writeRepository.saveAndFlush(write);    // UPDATE
            // 첨부파일(들) 정보 가져오기
            List<File> fileList = fileRepository.findByWrite(write.getId());
            setImage(fileList);
            write.setFileList(fileList);

            list.add(write);
        }

        return list;
    }

    // [이미지 파일 여부 세팅]
    private void setImage(List<File> fileList) {
        // upload 실제 물리적인 경로
        String realPath = new File(uploadDir).getAbsolutePath();

        for(File fileDto : fileList) {
            BufferedImage imgData = null;
            File f = new File(realPath, fileDto.getFile());  // 첨부파일에 대한 File 객체
            try {
                imgData = ImageIO.read(f);
                // ※ ↑ 파일이 존재 하지 않으면 IOExcepion 발생한다
                //   ↑ 이미지가 아닌 경우는 null 리턴
            } catch (IOException e) {
                System.out.println("파일존재안함: " + f.getAbsolutePath() + " [" + e.getMessage() + "]");
            }

            if(imgData != null) fileDto.setImage(true); // 이미지 여부 체크
        } // end for
    }

    public List<Board> list(){
        return writeRepository.findAll();
    }

    // 특정 id 의 글 읽어오기
    // 조회수 증가 없음
    public List<Board> selectById(long id) {
        List<Board> list = new ArrayList<>();

        Board write = writeRepository.findById(id).orElse(null);

        if(write != null){
            // 첨부파일 정보 가져오기
            List<File> fileList = fileRepository.findByWrite(write.getId());
            setImage(fileList);   // 이미지 파일 여부 세팅
//            write.setFiles(fileList);
            write.setFileList(fileList);

            list.add(write);
        }

        return list;
    }

    // 페이징 리스트
    public List<Board> list(Integer page, Model model){
        if(page == null) page = 1;
        if(page < 1) page = 1;

        HttpSession session = Util.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = 10;
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = 10;
        session.setAttribute("page", page);

        Page<Board> pageWrites = writeRepository.findAll(PageRequest.of(page-1, pageRows, Sort.by(Sort.Order.desc("id"))));

        long cnt = pageWrites.getTotalElements();

        int totalPage =  pageWrites.getTotalPages(); //총 몇 '페이지' 분량인가?

        // page 값 보정
        if(page > totalPage) page = totalPage;


        // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
        int startPage = ((int)((page - 1) / writePages) * writePages) + 1;
        int endPage = startPage + writePages - 1;
        if (endPage >= totalPage) endPage = totalPage;

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", Util.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        // 해당 페이지의 글 목록 읽어오기
        List<Board> list = pageWrites.getContent();
        model.addAttribute("list", list);

        return list;
    }



    public int update(Board write
            , Map<String, MultipartFile> files   // 새로 추가된 첨부파일들
            , Long[] delfile){   // 삭제될 첨부파일들
        int result = 0;

        // update 하고자 하는 것을 일단 읽어와야 한다
        Board w =writeRepository.findById(write.getId()).orElse(null);
        if(w != null){
            w.setSubject(write.getSubject());
            w.setContent(write.getContent());
            writeRepository.save(w);

            // 첨부파일 추가
            addFiles(files, write.getId());

            // 삭제할 첨부파일들은 삭제하기

            if(delfile != null){
                for(Long fileId : delfile){
                    File file = fileRepository.findById(fileId).orElse(null);
                    if(file != null){
                        delFile(file);   // 물리적으로 삭제
                        fileRepository.delete(file);  // dB 에서 삭제
                    }
                }
            }
            return 1;
        }

        return result;
    } // end update

    public int deleteById(long id){
        int result = 0;

        Write write = writeRepository.findById(id).orElse(null);
        if(write != null) {
            // 물리적으로 저장된 첨부파일(들) 삭제
            List<FileDTO> fileList = fileRepository.findByWrite(id);
            if(fileList != null && fileList.size() > 0) {
                for(FileDTO file : fileList) {
                    delFile(file);
                }
                // 글삭제 (참조하는 첨부파일, 댓글 등도 같이 삭제 될 것이다 ON DELETE CASCADE)
                writeRepository.delete(write);

                return 1;
            }

        }

        return result;
    }

    // 특정 첨부파일(id) 를 물리적으로 삭제
    private void delFile(FileDTO file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();

        File f = new File(saveDirectory, file.getFile()); // 물리적으로 저장된 파일들이 삭제 대상
        System.out.println("삭제시도--> " + f.getAbsolutePath());

        if (f.exists()) {
            if (f.delete()) { // 삭제!
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        } // end if
    }

}







