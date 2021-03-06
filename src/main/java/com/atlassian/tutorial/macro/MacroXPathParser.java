package com.atlassian.tutorial.macro;

import org.htmlcleaner.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by szi on 4/18/2017.
 */
public class MacroXPathParser {


    private final DocumentBuilderFactory builderFactory;
    private DocumentBuilder builder;

    public MacroXPathParser() {

        builderFactory = DocumentBuilderFactory.newInstance();
        builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public String parse(String contents, String expression) {
        Document htmlDocument = null;
        try {
            htmlDocument = builder.parse(new ByteArrayInputStream(contents.getBytes()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        XPath xpath = XPathFactory.newInstance().newXPath();
        String str = null;
        try {
            str = xpath.compile(expression).evaluate(htmlDocument);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        System.out.println(str);
        return str;
    }

    public String parseWithCleaner(String contents, String expression) {

//        TagNode tagNode = new HtmlCleaner().clean(
//                "<div><table><td id='1234 foo 5678'>Hello</td>");
        TagNode tagNode = new HtmlCleaner().clean(contents);
//        TagNode tagNode = new HtmlCleaner().clean("<p>Text of goal 1 page</p>\n" +
//                "<p>&nbsp;</p>\n" +
//                "<ac:structured-macro ac:name=\"details\" ac:schema-version=\"1\" ac:macro-id=\"8472a36b-d885-4d61-ac44-58e241294a5c\">\n" +
//                "   <ac:parameter ac:name=\"id\">Props</ac:parameter>\n" +
//                "   <ac:rich-text-body>\n" +
//                "      <table>\n" +
//                "         <colgroup>\n" +
//                "            <col />\n" +
//                "            <col />\n" +
//                "         </colgroup>\n" +
//                "         <tbody>\n" +
//                "            <tr>\n" +
//                "               <th>&nbsp;</th>\n" +
//                "               <td>\n" +
//                "                  <ac:link>\n" +
//                "                     <ri:page ri:content-title=\"Program 1\" />\n" +
//                "                  </ac:link>\n" +
//                "               </td>\n" +
//                "            </tr>\n" +
//                "            <tr>\n" +
//                "               <th>&nbsp;</th>\n" +
//                "               <td>&nbsp;</td>\n" +
//                "            </tr>\n" +
//                "         </tbody>\n" +
//                "      </table>\n" +
//                "   </ac:rich-text-body>\n" +
//                "</ac:structured-macro>\n" +
//                "<p>&nbsp;</p>\n" +
//                "<p>&nbsp;</p>");
        Document doc = null;
        try {
            doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        XPath xpath = XPathFactory.newInstance().newXPath();
        String str = null;
        try {
//            str = (String) xpath.evaluate("//div//td[contains(@id, 'foo')]/text()",
//                    doc, XPathConstants.STRING);
            str = (String) xpath.evaluate(expression,
                    doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        TagNode[] elementsHavingAttribute = tagNode.getElementsHavingAttribute("ri:content-title", true);
        List<String> programNames = new ArrayList<String>();
        for (TagNode node : elementsHavingAttribute) {
//            try {
//                node.evaluateXPath("//*[@content-title]");
//            } catch (XPatherException e) {
//                e.printStackTrace();
//            }
            programNames.add(node.getAttributeByName("ri:content-title").toString());
        }


        Object[] selectedPrograms = null;
        try {
            selectedPrograms = tagNode.evaluateXPath("//*[@content-title]");
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        System.out.println(str);
        return str;
    }

    public List<String> findProgramsForGoal(String contents) {

        TagNode tagNode = new HtmlCleaner().clean(contents);

        TagNode[] elementsHavingAttribute = tagNode.getElementsHavingAttribute("ri:content-title", true);
        List<String> programNames = new ArrayList<String>();
        for (TagNode node : elementsHavingAttribute) {
            programNames.add(node.getAttributeByName("ri:content-title").toString());
        }

        System.out.println(programNames);
        return programNames;
    }

    public List<String> findProjectsForProgram(String contents) {

        TagNode tagNode = new HtmlCleaner().clean(contents);

        TagNode[] elementsHavingAttribute = tagNode.getElementsHavingAttribute("ri:content-title", true);
        List<String> projectNames = new ArrayList<String>();
        for (TagNode node : elementsHavingAttribute) {
            projectNames.add(node.getAttributeByName("ri:content-title").toString());
        }

        System.out.println(projectNames);
        return projectNames;
    }

    public List<Project> parseDataForProgram(String contents) {
        TagNode tagNode = new HtmlCleaner().clean(contents);
        TagNode[] tds = tagNode.getElementsByName("td", true);
        BaseToken baseToken = tds[1].getAllChildren().get(0);
        String[] projectTitles = tds[1].getAllChildren().get(0).toString().split(",");
        String[] projectOwners = tds[3].getAllChildren().get(0).toString().split(",");
        String[] projectDeadlines = tds[5].getAllChildren().get(0).toString().split(",");
        List<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < projectTitles.length; i++) {
            projects.add(new Project(projectOwners[i], projectDeadlines[i], projectTitles[i]));
        }

        return projects;

    }

    public List<Project> parseDataForProject(String contents) {
        TagNode tagNode = new HtmlCleaner().clean(contents);
        TagNode[] tds = tagNode.getElementsByName("td", true);

        //Get Objectives
        String objectives = tds[0].getAllChildren().get(0).toString();
        //Get outcomes
        String outcomes = tds[1].getAllChildren().get(0).toString();
        //Owner
        String owners = tds[2].getAllChildren().get(0).toString();
        //Deadline
        String deadlines = tds[3].getAllChildren().get(0).toString();
        //Missed deadline
        String missedDeadlines = tds[4].getAllChildren().get(0).toString();


        String[] projectTitles = tds[1].getAllChildren().get(0).toString().split(",");
        String[] projectOwners = tds[3].getAllChildren().get(0).toString().split(",");
        String[] projectDeadlines = tds[5].getAllChildren().get(0).toString().split(",");
        List<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < projectTitles.length; i++) {
            projects.add(new Project(projectOwners[i], projectDeadlines[i], projectTitles[i]));
        }

        return projects;

    }

    public Goal parseDataForGoal(String contents) {
        TagNode tagNode = new HtmlCleaner().clean(contents);
        TagNode[] tds = tagNode.getElementsByName("td", true);

        //Get Objectives
        String title = tds[0].getAllChildren().get(0).toString();
        //Get outcomes
        String objectives = tds[1].getAllChildren().get(0).toString();
        //Owner
        String outcome = tds[2].getAllChildren().get(0).toString();
        //Deadline
        String owner = tds[3].getAllChildren().get(0).toString();
        //Missed deadline
        String deadline = tds[4].getAllChildren().get(0).toString();
        //Missed deadline
        String missedDeadline = tds[5].getAllChildren().get(0).toString();

        String dependencyAndAction = tds[6].getAllChildren().get(0).toString();
        Goal goal = new Goal(title, objectives, outcome, owner, deadline, missedDeadline, dependencyAndAction);
        return goal;

    }

    public Program parseDataForExtendedProgram(String contents) {
        TagNode tagNode = new HtmlCleaner().clean(contents);
        TagNode[] tds = tagNode.getElementsByName("td", true);


        String title = tds[0].getAllChildren().get(0).toString();

        String objectives = tds[1].getAllChildren().get(0).toString();

        String outcome = tds[2].getAllChildren().get(0).toString();

        String owner = tds[3].getAllChildren().get(0).toString();

        String deadline = tds[4].getAllChildren().get(0).toString();

        String missedDeadline = tds[5].getAllChildren().get(0).toString();

        String dependencyAndAction = tds[6].getAllChildren().get(0).toString();

        String linkToGoal = tagNode.getElementsByName("ri:page",true)[0].getAttributeByName("ri:content-title").toString();
        Program program = new Program(title, objectives, outcome, owner, deadline, missedDeadline, dependencyAndAction,linkToGoal);
        return program;

    }

    public ExtendedProject parseDataForExtendedProject(String contents) {
        TagNode tagNode = new HtmlCleaner().clean(contents);
        TagNode[] tds = tagNode.getElementsByName("td", true);


        String title = tds[0].getAllChildren().get(0).toString();

        String objectives = tds[1].getAllChildren().get(0).toString();

        String outcome = tds[2].getAllChildren().get(0).toString();

        String owner = tds[3].getAllChildren().get(0).toString();

        String deadline = tds[4].getAllChildren().get(0).toString();

        String missedDeadline = tds[5].getAllChildren().get(0).toString();

        String dependencyAndAction = tds[6].getAllChildren().get(0).toString();

        String linkToProgram = tagNode.getElementsByName("ri:page",true)[0].getAttributeByName("ri:content-title").toString();
        ExtendedProject project = new ExtendedProject(title, objectives, outcome, owner, deadline, missedDeadline, dependencyAndAction,linkToProgram);
        return project;

    }
}
