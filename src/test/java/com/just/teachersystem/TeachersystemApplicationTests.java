package com.just.teachersystem;


import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Mapper.CommonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeachersystemApplicationTests {
    @Autowired
    CommonMapper commonMapper;
    @Test
    public void contextLoads() {


    }
//    @Test
//    public void Test1(){
//        List<Kind> list=commonMapper.getTypeList();
//        for (Kind kind:
//             list) {
//            System.out.println(kind);
//        }
//    }


}
