package com.harshproject.fileEncrypDecryp.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harshproject.fileEncrypDecryp.model.FileModel;
import com.harshproject.fileEncrypDecryp.repo.FileRepository;

@Service
public class FileService {
@Autowired
    private FileRepository fileRepository;


    public List<FileModel> getAllFiles(){
        return fileRepository.findAll();
    }

    public FileModel  saveFile(FileModel file){

        return fileRepository.save(file);
    } 
}
