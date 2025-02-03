package com.harshproject.fileEncrypDecryp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

import com.harshproject.fileEncrypDecryp.model.FileModel;
import com.harshproject.fileEncrypDecryp.service.FileService;

@RestController
@RequestMapping("api/files")
public class FileController {
    
    @Autowired
    private  FileService fileService;


    @GetMapping
    public List<FileModel> getAllFiles(){
        return fileService.getAllFiles();
    }

    
    
    @PostMapping
    public FileModel uploadFile(@RequestBody FileModel file){
        return fileService.saveFile(file);
    }

}
