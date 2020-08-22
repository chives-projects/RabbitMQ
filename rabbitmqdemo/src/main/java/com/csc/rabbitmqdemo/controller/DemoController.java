package com.csc.rabbitmqdemo.controller;

import com.csc.rabbitmqdemo.template.send.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @PackageName: com.csc.rabbitmqdemo.controller
 * @Author: csc
 * @Create: 2020-08-21 9:47
 * @Version: 1.0
 */
@RestController
@RequestMapping("rabbit")
@Api(tags = "rabbit")
public class DemoController {
    @Autowired
    private HelloSender helloSender;
    @Autowired
    private HelloSender2 helloSender2;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private DirectSender directSender;
    @Autowired
    private HeadersSender headersSender;
    @Autowired
    private TransactionSender transactionSender;

    @ApiOperation("oneToOne")
    @GetMapping("oneToOne")
    public void oneToOne(){
        helloSender.send("oneToOne");
    }

    @ApiOperation("oneToMany")
    @GetMapping("oneToMany")
    public void oneToMany() {
        for (int i = 0; i < 4; i++) {
            helloSender.send("第[" + (i + 1) + "]个 ---------> ");
        }
    }

    @ApiOperation("manyToMany")
    @GetMapping("manyToMany")
    public void manyToMany() {
        for (int i = 0; i < 4; i++) {
            helloSender.send("第[" + (i + 1) + "]个 ---------> ");
            helloSender2.send("第[" + (i + 1) + "]个 ---------> ");
        }
    }

    @ApiOperation("topicTest")
    @GetMapping("topicTest")
    public void topicTest() {
        topicSender.send();
    }


    @ApiOperation("fanoutTest")
    @GetMapping("fanoutTest")
    public void fanoutTest() {
        fanoutSender.send();
    }

    @ApiOperation("directTest")
    @GetMapping("directTest")
    public void directTest() {
        directSender.send();
    }

    @ApiOperation("headersTest")
    @GetMapping("headersTest")
    public void headersTest() {
        headersSender.send();
    }

    /**
     * 事务消息发送测试
     */
    @ApiOperation("transition")
    @GetMapping("transition")
    public void transition() {
        transactionSender.send("Transition:  ");
    }
}
