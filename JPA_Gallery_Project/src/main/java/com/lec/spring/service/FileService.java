package com.lec.spring.service;

import com.lec.spring.domain.FileDTO;
import com.lec.spring.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileService {
    private FileRepository fileRepository;

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public FileService(){
        System.out.println("FileService() 생성");
    }

    public FileDTO findById(Long id){
        return fileRepository.findById(id).orElse(null);
    }
}