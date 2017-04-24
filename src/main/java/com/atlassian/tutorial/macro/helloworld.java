package com.atlassian.tutorial.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.util.*;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.spring.container.ContainerManager;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.springframework.beans.factory.annotation.Autowired;

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
        //return generateSimpleHtmlTable();
        return generateTableWithDivs();
    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }

    public String generateSimpleHtmlTable() {

        StringBuilder builder = new StringBuilder();

        PageManager pageManager = (PageManager) ContainerManager.getComponent("pageManager");

        Space TEST_SPACE = spaceManager.getSpace(SPACE_KEY);

        List<Page> PagesInTestSpace = pageManager.getPages(TEST_SPACE, false);

        Label goal = new Label("goal");
        Label program = new Label("program");

        List<Page> goalPages = new ArrayList<Page>();
        List<Page> programPages = new ArrayList<Page>();
        for (Page page : PagesInTestSpace) {

            if (page.getLabels().contains(goal)) {
                goalPages.add(page);
            }
            if (page.getLabels().contains(program)) {
                programPages.add(page);
            }

        }

        HashMap<String, Page> titleToProgram = new LinkedHashMap<String, Page>();
        for (Page programPage : programPages) {
            titleToProgram.put(programPage.getTitle(), programPage);
        }

        builder.append("<table>");
        builder.append("<tr><th>Goals</th><th>Programs</th><th>Projects</th></tr>");
        for (Page goalIterator : goalPages) {
            String content = goalIterator.getBodyContents().get(0).getContent().getBodyAsString();
            List<String> programsForGoal = parser.findProgramsForGoal(content);
            //Draw table for only existing programs for goal
            if (!programsForGoal.isEmpty()) {
                for (String programTitleIterator : programsForGoal) {

                    Page programPage = titleToProgram.get(programTitleIterator);
                    List<String> projectTitlesForProgram = parser.findProjectsForProgram(programPage.getBodyContents().get(0).getContent().getBodyAsString());
                    System.out.println("Project titles for program: " + projectTitlesForProgram);
                    //Draw table for only existing projects for Program
                    if (!projectTitlesForProgram.isEmpty()) {
                        for (String projectTitleForProgramIterator : projectTitlesForProgram) {
                            builder.append("<tr>");
                            builder.append("<td>" + goalIterator.getTitle() + "</td>");
                            builder.append("<td>" + programTitleIterator + "</td>");
                            builder.append("<td>" + projectTitleForProgramIterator + "</td>");
                            builder.append("</tr>");
                        }
                    } else {
                        builder.append("<tr>");
                        builder.append("<td>" + goalIterator.getTitle() + "</td>");
                        builder.append("<td>" + programTitleIterator + "</td>");
                        builder.append("<td>" + "</td>");
                        builder.append("</tr>");
                    }
                }
            }
        }

        builder.append("</table>");

        return builder.toString();
    }

    public String generateTable2() {

        StringBuilder builder = new StringBuilder();
        builder.append("<div class=\"container-fluid\" " +
                "  <div class=\"table\"> \n " +
                "  <div class=\"table-row header\">\n" +
                "    <div class=\"column goals\">Goals</div>\n" +
                "    <div class=\"column programs\">Programs</div>\n" +
                "    <div class=\"column projects\">Projects</div>\n" +
                "    <div class=\"column objectives\">Objectives</div>\n" +
                "    <div class=\"column outcome\">Outcome</div>\n" +
                "    <div class=\"column outcome\">Owner</div>\n" +
                "    <div class=\"column deadline\">Deadline</div>\n" +
                "    <div class=\"column missed\">Missed deadlines</div>\n" +
                "    <div class=\"column actions\">Dependency and Actions</div>\n" +
                "  </div>\n");

        builder.append("<div class=\"table-row\">\n" +
                "    <div class=\"column goals\">Protect the GM</div>\n" +
                "    <div class=\"column programs\"></div>\n" +
                "    <div class=\"column projects\"></div>\n" +
                "    <div class=\"column objectives\"></div>\n" +
                "    <div class=\"column outcome\"></div>\n" +
                "    <div class=\"column outcome\"></div>\n" +
                "    <div class=\"column deadline\"></div>\n" +
                "    <div class=\"column missed\"></div>\n" +
                "    <div class=\"column actions\"></div>\n" +
                "  </div>\n" +
                "    \n" +
                "  <div class=\"table-row\">\n" +
                "    <div class=\"column goals\"></div>\n" +
                "    <div class=\"column programs\">Technology Strategy</div>\n" +
                "    <div class=\"column projects\"></div>\n" +
                "    <div class=\"column objectives\"></div>\n" +
                "    <div class=\"column outcome\"></div>\n" +
                "    <div class=\"column outcome\"></div>\n" +
                "    <div class=\"column deadline\"></div>\n" +
                "    <div class=\"column missed\"></div>\n" +
                "    <div class=\"column actions\"></div>\n" +
                "  </div>\n" +
                "    \n" +
                "  <div class=\"table-row\">\n" +
                "    <div class=\"column goals\"></div>\n" +
                "    <div class=\"column programs\"></div>\n" +
                "    <div class=\"column projects\">OLA by Solutions organization for Managed Team and Fixed Project delivery and support</div>\n" +
                "    <div class=\"column objectives\">+</div>\n" +
                "    <div class=\"column outcome\">+</div>\n" +
                "    <div class=\"column outcome\">Someone else</div>\n" +
                "    <div class=\"column deadline\">31.05.2017<br />31.05.2017<br />31.05.2017<br />31.05.2017<br /></div>\n" +
                "    <div class=\"column missed\">24.03.2017</div>\n" +
                "    <div class=\"column actions\">Do something!</div>\n" +
                "  </div>");

        builder.append("</div>\n" +
                "</div>");

        return builder.toString();
    }

    String generateTableWithDivs() {
        StringBuilder builder = new StringBuilder();

        PageManager pageManager = (PageManager) ContainerManager.getComponent("pageManager");

        Space TEST_SPACE = spaceManager.getSpace(SPACE_KEY);

        List<Page> PagesInTestSpace = pageManager.getPages(TEST_SPACE, false);

        Label goal = new Label("goal");
        Label program = new Label("program");

        List<Page> goalPages = new ArrayList<Page>();
        List<Page> programPages = new ArrayList<Page>();
        for (Page page : PagesInTestSpace) {

            if (page.getLabels().contains(goal)) {
                goalPages.add(page);
            }
            if (page.getLabels().contains(program)) {
                programPages.add(page);
            }

        }

        HashMap<String, Page> titleToProgram = new LinkedHashMap<String, Page>();
        for (Page programPage : programPages) {
            titleToProgram.put(programPage.getTitle(), programPage);
        }

        builder.append("<div class=\"container-fluid\" style=\"margin-top: 10px\">\n" +
                "  <div class=\"macro-table\">");
        builder.append("<div class=\"macro-table-row header\">\n" +
                "    <div class=\"macro-column goals\">Goals</div>\n" +
                "    <div class=\"macro-column programs\">Programs</div>\n" +
                "    <div class=\"macro-column projects\">Projects</div>\n" +
                "    <div class=\"macro-column objectives\">Objectives</div>\n" +
                "    <div class=\"macro-column outcome\">Outcome</div>\n" +
                "    <div class=\"macro-column outcome\">Owner</div>\n" +
                "    <div class=\"macro-column deadline\">Deadline</div>\n" +
                "    <div class=\"macro-column missed\">Missed deadlines</div>\n" +
                "    <div class=\"macro-column actions\">Dependency and Actions</div>\n" +
                "  </div>");
        for (Page goalIterator : goalPages) {
            builder.append("<div class=\"macro-table-row\">\n" +
                    "    <div class=\"macro-column goals\"> " + goalIterator.getTitle() + " </div>\n" +
                    "    <div class=\"macro-column programs\"></div>\n" +
                    "    <div class=\"macro-column projects\"></div>\n" +
                    "    <div class=\"macro-column objectives\"></div>\n" +
                    "    <div class=\"macro-column outcome\"></div>\n" +
                    "    <div class=\"macro-column outcome\"></div>\n" +
                    "    <div class=\"macro-column deadline\"></div>\n" +
                    "    <div class=\"macro-column missed\"></div>\n" +
                    "    <div class=\"macro-column actions\"></div>\n" +
                    "  </div>");
            String content = goalIterator.getBodyContents().get(0).getContent().getBodyAsString();
            List<String> programsForGoal = parser.findProgramsForGoal(content);
            //Draw table for only existing programs for goal
            if (!programsForGoal.isEmpty()) {
                for (String programTitleIterator : programsForGoal) {

                    builder.append("<div class=\"macro-table-row\">\n" +
                            "    <div class=\"macro-column goals\"></div>\n" +
                            "    <div class=\"macro-column programs\"> " + programTitleIterator + "</div>\n" +
                            "    <div class=\"macro-column projects\"></div>\n" +
                            "    <div class=\"macro-column objectives\"></div>\n" +
                            "    <div class=\"macro-column outcome\"></div>\n" +
                            "    <div class=\"macro-column outcome\"></div>\n" +
                            "    <div class=\"macro-column deadline\"></div>\n" +
                            "    <div class=\"macro-column missed\"></div>\n" +
                            "    <div class=\"macro-column actions\"></div>\n" +
                            "  </div>");

                    Page programPage = titleToProgram.get(programTitleIterator);
                    List<String> projectTitlesForProgram = parser.findProjectsForProgram(programPage.getBodyContents().get(0).getContent().getBodyAsString());
                    System.out.println("Project titles for program: " + projectTitlesForProgram);
                    //Draw table for only existing projects for Program
                    if (!projectTitlesForProgram.isEmpty()) {
                        for (String projectTitleForProgramIterator : projectTitlesForProgram) {
                            builder.append(" <div class=\"macro-table-row\">\n" +
                                    "    <div class=\"macro-column goals\"></div>\n" +
                                    "    <div class=\"macro-column programs\"></div>\n" +
                                    "    <div class=\"macro-column projects\">" + projectTitleForProgramIterator + "</div>\n" +
                                    "    <div class=\"macro-column objectives\">+</div>\n" +
                                    "    <div class=\"macro-column outcome\">+</div>\n" +
                                    "    <div class=\"macro-column outcome\">Someone else</div>\n" +
                                    "    <div class=\"macro-column deadline\">31.05.2017<br />31.05.2017<br />31.05.2017<br />31.05.2017<br /></div>\n" +
                                    "    <div class=\"macro-column missed\">24.03.2017</div>\n" +
                                    "    <div class=\"macro-column actions\">Do something!</div>\n" +
                                    "  </div>");
                        }
                    } else {
//                        builder.append("<tr>");
//                        builder.append("<td>" + goalIterator.getTitle() + "</td>");
//                        builder.append("<td>" + programTitleIterator + "</td>");
//                        builder.append("<td>" + "</td>");
//                        builder.append("</tr>");
                    }
                }
            }
        }

        builder.append("</div>\n" +
                "</div>");

        return builder.toString();
    }
}