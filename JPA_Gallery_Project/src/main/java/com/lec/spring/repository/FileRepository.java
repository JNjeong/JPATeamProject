package com.lec.spring.repository;

import com.lec.spring.domain.FileDTO;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FileRepository extends JpaRepository<FileDTO, Long> {
    // 특정 글(write)의 첨부파일들
    List<FileDTO> findByWrite(Long writeId);

}
