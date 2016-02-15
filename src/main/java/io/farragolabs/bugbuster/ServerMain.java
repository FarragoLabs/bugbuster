package io.farragolabs.bugbuster;

import io.farragolabs.bugbuster.route.*;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.File;

import static spark.Spark.halt;

public class ServerMain {
    public static void main(String[] args) {

        HandlebarsTemplateEngine handlebarsTemplateEngine = new HandlebarsTemplateEngine();

        setup();
        Spark.port(9090);

        Spark.get("/", new HomeRedirect());

        //--User authentication model--//
        Spark.get("/create-user", new CreateUser(handlebarsTemplateEngine));
        Spark.post("/create-user", new PostCreateUser());
        Spark.get("/login", new ShowLogin(handlebarsTemplateEngine));
        Spark.post("/login", new PostLogin());
        Spark.post("/logout", new Logout());

        //------App----------//
        Spark.get("/v1", new BugBusterHome(handlebarsTemplateEngine));
        Spark.get("/v1/app", new AppList(handlebarsTemplateEngine));
        Spark.get("/v1//app/:appname", new App());
        Spark.get("/v1//create-app", new CreateApp());
        Spark.post("/v1//comments", new SaveComments());
        Spark.get("/v1//create-issue/:appname", new CreateIssuePage());
        Spark.post("/v1//create-issue/:appname", new CreateIssue());
        Spark.get("/v1//creating-issue/:appname", new CreateIssue());
        Spark.get("/v1//bug/:bugid", new Bug());
        Spark.get("/v1//upload", new Upload());
        Spark.post("/v1//bugSave", new BugSave());
        Spark.get("/v1//link/:bug_id1/to/:bug_id2", new CreateLink());

        Spark.get("/login", new ShowLogin(handlebarsTemplateEngine));
        Spark.post("/login", new PostLogin());


        Spark.before("/v1", new AuthenticationFilter());
        Spark.before("/v1/*", new AuthenticationFilter());
    }

    private static void setup() {
        BugListConfigurationModel.BUG_BUSTER_HOME = System.getProperty("bugify.home");
        if (BugListConfigurationModel.BUG_BUSTER_HOME == null) {
            BugListConfigurationModel.BUG_BUSTER_HOME = "bugify-workspace/";
        }

        BugListConfigurationModel.BUG_BUSTER_DIR = new File(BugListConfigurationModel.BUG_BUSTER_HOME);

        if (BugListConfigurationModel.BUG_BUSTER_USER_DIR == null) {
            BugListConfigurationModel.BUG_BUSTER_USER_DIR = new File("bugify-user-dir/");
        }

        System.out.println(BugListConfigurationModel.BUG_BUSTER_DIR.mkdir());
        System.out.println(BugListConfigurationModel.BUG_BUSTER_USER_DIR.mkdir());

    }
}
