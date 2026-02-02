package com.dacoach.service.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dacoach.mapper.classes.ClassMapper;
import com.dacoach.model.classes.ClassDTO;

@Service
public class ClassServiceImple implements ClassService {
    
    @Autowired
    private ClassMapper classMapper;
    
    @Override
    public int classRegister(ClassDTO classDTO) {
        return classMapper.insertClass(classDTO);
    }
}
