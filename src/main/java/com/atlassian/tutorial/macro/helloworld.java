package com.atlassian.tutorial.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.BodyContent;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.DocumentBuilder;

@Scanned
public class helloworld implements Macro {

    private PageBuilderService pageBuilderService;
    private SpaceManager spaceManager;
    private final String SPACE_KEY = "TES";
    private MacroXPathParser parser = new MacroXPathParser();

    @Autowired
    public helloworld(@ComponentImport PageBuilderService pageBuilderService, @ComponentImport SpaceManager spaceManager) {
        this.pageBuilderService = pageBuilderService;
        this.spaceManager = spaceManager;
    }


    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {


        pageBuilderService.assembler().resources().requireWebResource("com.atlassian.tutorial.myConfluenceMacro:myConfluenceMacro-resources");


        PageManager pageManager = (PageManager)ContainerManager.getComponent("pageManager");

        List<Space> allSpaces = spaceManager.getAllSpaces();

        Space TEST_SPACE = spaceManager.getSpace(SPACE_KEY);

        List<Page> PagesInTestSpace = pageManager.getPages(TEST_SPACE, false);


        String output = "<div class =\"helloworld\">";
        output = output + "<div class = \"" + map.get("Color") + "\">";

//
//        if (map.get("Name") != null) {
//            output = output + ("<h1>Hello " + map.get("Name") + "!</h1>");
//        } else {
//            output = output + "<h1>Hello World!<h1>";
//        }
//
//        for (Space space: allSpaces) {
//            output = output + "<h1>" + space.getName() + "</h1>";
//        }

        Label goal = new Label("goal");
        output = output + "<h1>Pages in TestSpace </h1>";

        // Print out only goal pages
        output = output + "<h1>GOAL Pages in TestSpace </h1>";

        List<Page> goalPages = new ArrayList<Page>();
        for (Page page: PagesInTestSpace) {

            if (page.getLabels().contains(goal)){
                output = output + "<h1>" + page.getTitle() + "</h1>";
                goalPages.add(page);
                //How to get GOAL 1 page content
                //page.bodyContents.get(0).getContent().getBodyAsString();
                output = output + "<h1>Program pages for " + page.getTitle() + "</h1>";
                String content = page.getBodyContents().get(0).getContent().getBodyAsString();
             //   String expression = "//structured-macro[@name=\"details\"]//table/tbody//tr//td/
//                String expression = "//ac:structured-macro[@ac:name=\"details\"]//ac:reach-text-body/text()";
               // String expression = "//p[1][text()]";

                //Working Xpath expression to get title for one programm
                String expression = "//structured-macro//link//page[@content-title]/@content-title";

                String parsed = parser.parseWithCleaner(content,expression);

                List<String> programsForGoal = parser.findProgramsForGoal(content);

                for (String programName: programsForGoal){
                    output = output + "<h1>" + programName + "</h1>";
                }

                output = output + "<h1>" + parsed + "</h1>";

            }
        }

        Label programm = new Label("program");

        output = output + "<h1>Programm Pages in TestSpace </h1>";
        for (Page page: PagesInTestSpace) {

            if (page.getLabels().contains(programm)){
                output = output + "<h1>" + page.getTitle() + "</h1>";
//                List<BodyContent> bodyContents = page.getBodyContents();
//                BodyContent bodyContent = bodyContents.get(0);
//                bodyContent.getContent();
            }
        }

        output = output + "</div>" + "</div>";

        output = output + "<table>" +
                "<tr><th>Goals</th><th>Programs</th></tr>";
        for (Page goalIterator: goalPages) {
            output = output + "<tr>";
            output = output + "<td>" + goalIterator.getTitle() + "</td>";
            String content = goalIterator.getBodyContents().get(0).getContent().getBodyAsString();
            List<String> programsForGoal = parser.findProgramsForGoal(content);
            output = output + "<td>" + programsForGoal.toString() + "</td>";
            output = output + "</tr>";

        }

        output = output + "</table>";


        // Return all formed html content for macro
        return output;
    }

    public BodyType getBodyType() { return BodyType.NONE; }

    public OutputType getOutputType() { return OutputType.BLOCK; }
}