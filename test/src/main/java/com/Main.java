package com;

import com.service.command.ExecuteCommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private ExecuteCommandsService executeCommandService;

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] {"ApplicationContext.xml"});
        Main main = context.getBean(Main.class);
        main.getExecuteCommandService().excute();
    }
    public ExecuteCommandsService getExecuteCommandService() {
        return executeCommandService;
    }

}
