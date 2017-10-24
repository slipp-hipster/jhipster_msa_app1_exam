package net.slipp.jhipster.cucumber.stepdefs;

import net.slipp.jhipster.App1App;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = App1App.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
