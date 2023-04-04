package com.sipc.userserver.controller;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.result.AcaMajorInfo;
import com.sipc.userserver.service.AcaMajorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class AcaMajorController {
    private final AcaMajorService acaMajorService;

    @Autowired
    public AcaMajorController(AcaMajorService acaMajorService) {
        this.acaMajorService = acaMajorService;
    }

    @GetMapping("/acamajor/get")
    public CommonResult<List<AcaMajorInfo>> getAllAcaMajorInfo(){
        return acaMajorService.getAllAcamajorInfo();
    }
}
