package com.lec.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    // 특정 글(write)의 첨부파일들
    List<File> findByWrite(Long writeId);

}
