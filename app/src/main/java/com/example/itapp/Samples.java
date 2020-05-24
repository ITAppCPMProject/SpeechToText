package com.example.itapp;

import androidx.annotation.Keep;

public class Samples {

        String name;
        String sample_content;
        String sample_path;

       Samples(){

       }

        Samples(String sample_content,String name, String sample_path){
               this.name=name;
               this.sample_content=sample_content;
               this.sample_path=sample_path;
        }

        public void setName(String nam){
                this.name=nam;
        }
        public void setSample_content(String content){
                this.sample_content=content;
        }
        public void setSample_path(String path){
                this.sample_path=path;
        }

        public String getName()
        {
                return this.name;
        }

        public String getContent()
        {
                return this.sample_content;
        }
        public String getPath(){
                return this.sample_path;
        }

}


