package com.xiaomaotongzhi.huilan.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attention")
@Api(tags = "关注接口")
public class AttentionController extends BaseController {

}
