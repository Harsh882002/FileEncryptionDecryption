package com.harshproject.fileEncrypDecryp.repo;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.harshproject.fileEncrypDecryp.model.FileModel;

public interface FileRepository extends JpaRepository<FileModel,Long> {

}
