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
    private final String SPACE_KEY = "PROG";
    private MacroXPathParser parser = new MacroXPathParser();

    @Autowired
    public helloworld(@ComponentImport PageBuilderService pageBuilderService, @ComponentImport SpaceManager spaceManager) {
        this.pageBuilderService = pageBuilderService;
        this.spaceManager = spaceManager;
    }


    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        pageBuilderService.assembler().resources().requireWebResource("com.atlassian.tutorial.myConfluenceMacro:myConfluenceMacro-resources");
        //return generateSimpleHtmlTable();

        //  return generateTableWithDivs();

        //  return generateTableWithDivsFull();

        // return checkStructure();

        return generateProgramReport();
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

    public String generateTableWithDivs() {
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

    public String generateTableWithDivsFull() {
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
                    // List<String> projectTitlesForProgram = parser.findProjectsForProgram(programPage.getBodyContents().get(0).getContent().getBodyAsString());
                    //  System.out.println("Project titles for program: " + projectTitlesForProgram);
                    List<Project> projects = parser.parseDataForProgram(programPage.getBodyContents().get(0).getContent().getBodyAsString());
                    //Draw table for only existing projects for Program
                    if (!projects.isEmpty()) {
                        for (Project projectTitleForProgramIterator : projects) {
                            builder.append(" <div class=\"macro-table-row\">\n" +
                                    "    <div class=\"macro-column goals\"></div>\n" +
                                    "    <div class=\"macro-column programs\"></div>\n" +
                                    "    <div class=\"macro-column projects\">" + projectTitleForProgramIterator.getTitle() + "</div>\n" +
                                    "    <div class=\"macro-column objectives\">+</div>\n" +
                                    "    <div class=\"macro-column outcome\">+</div>\n" +
                                    "    <div class=\"macro-column outcome\">" + projectTitleForProgramIterator.getOwner() + "</div>\n" +
                                    "    <div class=\"macro-column deadline\">" + projectTitleForProgramIterator.getDeadline() + "</div>\n" +
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

    public String checkStructure() {
        PageManager pageManager = (PageManager) ContainerManager.getComponent("pageManager");

        Space TEST_SPACE = spaceManager.getSpace(SPACE_KEY);

        List<Page> PagesInTestSpace = pageManager.getPages(TEST_SPACE, false);

        Label goal = new Label("goal");
        Label program = new Label("program");
        Label project = new Label("project");

        List<Page> goalPages = new ArrayList<Page>();
        List<Page> programPages = new ArrayList<Page>();
        List<Page> projectPages = new ArrayList<Page>();
        for (Page page : PagesInTestSpace) {

            if (page.getLabels().contains(goal)) {
                goalPages.add(page);
            }
            if (page.getLabels().contains(program)) {
                programPages.add(page);
            }
            if (page.getLabels().contains(project)) {
                projectPages.add(page);
            }

        }

        HashMap<String, Page> titleToProgram = new LinkedHashMap<String, Page>();
        for (Page programPage : programPages) {
            titleToProgram.put(programPage.getTitle(), programPage);
        }


//        Page goalPage = goalPages.get(0);
//
//        String content = goalPage.getBodyContents().get(0).getContent().getBodyAsString();
//
//        Goal goalObject = parser.parseDataForGoal(content);

//        Page programPage = programPages.get(0);
//
//        String content = programPage.getBodyContents().get(0).getContent().getBodyAsString();
//
//        Program programObject = parser.parseDataForExtendedProgram(content);

        Page projectPage = projectPages.get(0);

        String content = projectPage.getBodyContents().get(0).getContent().getBodyAsString();

        ExtendedProject projectObject = parser.parseDataForExtendedProject(content);
        return projectObject.toString();
    }

    public String generateProgramReport() {
        PageManager pageManager = (PageManager) ContainerManager.getComponent("pageManager");

        Space TEST_SPACE = spaceManager.getSpace(SPACE_KEY);

        List<Page> PagesInTestSpace = pageManager.getPages(TEST_SPACE, false);

        Label goal = new Label("goal");
        Label program = new Label("program");
        Label project = new Label("project");

        List<Page> goalPages = new ArrayList<Page>();
        List<Page> programPages = new ArrayList<Page>();
        List<Page> projectPages = new ArrayList<Page>();

        for (Page page : PagesInTestSpace) {

            if (page.getLabels().contains(goal)) {
                goalPages.add(page);
            }
            if (page.getLabels().contains(program)) {
                programPages.add(page);
            }
            if (page.getLabels().contains(project)) {
                projectPages.add(page);
            }

        }

        Map<String, Page> titleToProjectPages = new LinkedHashMap<String, Page>();
        Map<String, Page> titleToProgramPages = new LinkedHashMap<String, Page>();
        Map<String, Page> titleToGoalPages = new LinkedHashMap<String, Page>();

        Map<String, ExtendedProject> titleToProjectObject = new LinkedHashMap<String, ExtendedProject>();
        Map<String, Program> titleToProgramObject = new LinkedHashMap<String, Program>();
        Map<String, Goal> titleToGoalObject = new LinkedHashMap<String, Goal>();

        List<ExtendedProject> projectObjects = new ArrayList<ExtendedProject>();
        List<Goal> goalObjects = new ArrayList<Goal>();
        List<Program> programObjects = new ArrayList<Program>();

        for (Page page : goalPages) {
            titleToGoalPages.put(page.getTitle(), page);
            String content = page.getBodyContents().get(0).getContent().getBodyAsString();
            Goal parsedGoal = parser.parseDataForGoal(content);
            goalObjects.add(parsedGoal);
            titleToGoalObject.put(page.getTitle(), parsedGoal);

        }
        for (Page page : programPages) {
            titleToProgramPages.put(page.getTitle(), page);
            String content = page.getBodyContents().get(0).getContent().getBodyAsString();
            Program parsedProgram = parser.parseDataForExtendedProgram(content);
            programObjects.add(parsedProgram);
            titleToProgramObject.put(page.getTitle(), parsedProgram);
        }
        for (Page page : projectPages) {
            titleToProjectPages.put(page.getTitle(), page);
            String content = page.getBodyContents().get(0).getContent().getBodyAsString();
            ExtendedProject parsedProject = parser.parseDataForExtendedProject(content);
            projectObjects.add(parsedProject);
            titleToProjectObject.put(page.getTitle(), parsedProject);
        }

        StringBuilder builder = new StringBuilder();

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

        for (Goal goalIterator : goalObjects) {
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

            List<Program> programsForGoal = findProgramsForGoal(programObjects, goalIterator.getTitle());
            //Draw table for only existing programs for goal
            if (!programsForGoal.isEmpty()) {
                for (Program programIterator : programsForGoal) {

                    builder.append("<div class=\"macro-table-row\">\n" +
                            "    <div class=\"macro-column goals\"></div>\n" +
                            "    <div class=\"macro-column programs\"> " + programIterator.getTitle() + "</div>\n" +
                            "    <div class=\"macro-column projects\"></div>\n" +
                            "    <div class=\"macro-column objectives\">" + programIterator.getObjectives() +"</div>\n" +
                            "    <div class=\"macro-column outcome\">" +  programIterator.getOutcome()+"</div>\n" +
                            "    <div class=\"macro-column outcome\">"+  programIterator.getOwner()+"</div>\n" +
                            "    <div class=\"macro-column deadline\">"+  programIterator.getDeadline()+"</div>\n" +
                            "    <div class=\"macro-column missed\">"+  programIterator.getMissedDeadline()+"</div>\n" +
                            "    <div class=\"macro-column actions\">" +  programIterator.getDependencyAndAction()+"</div>\n" +
                            "  </div>");

//                    Page programPage = titleToProgram.get(programIterator);
//                    // List<String> projectTitlesForProgram = parser.findProjectsForProgram(programPage.getBodyContents().get(0).getContent().getBodyAsString());
//                    //  System.out.println("Project titles for program: " + projectTitlesForProgram);
//                    List<Project> projects = parser.parseDataForProgram(programPage.getBodyContents().get(0).getContent().getBodyAsString());
//                    //Draw table for only existing projects for Program
                    List<ExtendedProject> projectsForProgram = findProjectsForProgram(projectObjects, programIterator.getTitle());
                    if (!projectsForProgram.isEmpty()) {
                        for (ExtendedProject projectForProgramIterator : projectsForProgram) {
                            builder.append(" <div class=\"macro-table-row\">\n" +
                                    "    <div class=\"macro-column goals\"></div>\n" +
                                    "    <div class=\"macro-column programs\"></div>\n" +
                                    "    <div class=\"macro-column projects\">" + projectForProgramIterator.getTitle() + "</div>\n" +
                                    "    <div class=\"macro-column objectives\">"+ projectForProgramIterator.getObjectives()+ "</div>\n" +
                                    "    <div class=\"macro-column outcome\">" + projectForProgramIterator.getOutcome()+"</div>\n" +
                                    "    <div class=\"macro-column outcome\">" + projectForProgramIterator.getOwner() + "</div>\n" +
                                    "    <div class=\"macro-column deadline\">" + projectForProgramIterator.getDeadline() + "</div>\n" +
                                    "    <div class=\"macro-column missed\">"+ projectForProgramIterator.getMissedDeadline()+ "</div>\n" +
                                    "    <div class=\"macro-column actions\">" + projectForProgramIterator.getDependencyAndAction() + "</div>\n" +
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


//        Page projectPage = projectPages.get(0);
//
//        String content = projectPage.getBodyContents().get(0).getContent().getBodyAsString();
//
//        ExtendedProject projectObject = parser.parseDataForExtendedProject(content);
//        return projectObject.toString();
    }

    private List<Program> findProgramsForGoal(List<Program> programObjects, String goalLink) {
        List<Program> result = new ArrayList<Program>();
        for (Program program : programObjects) {
            System.out.println("program.getLinkToGoal():" + program.getLinkToGoal());
            System.out.println("goalLink:" + goalLink);
            if (program.getLinkToGoal().equals(goalLink)) {
                result.add(program);
            }
        }
        return result;
    }

    private List<ExtendedProject> findProjectsForProgram(List<ExtendedProject> projectObjects, String programLink) {
        List<ExtendedProject> result = new ArrayList<ExtendedProject>();
        for (ExtendedProject project : projectObjects) {

            if (project.getLinkToProgram().equals(programLink)) {
                result.add(project);
            }
        }
        return result;
    }
}
