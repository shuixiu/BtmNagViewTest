package com.test.sixpro.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class IndutyMustBean implements Serializable {

    private String return_code;
    private String return_msg;
    private  IndutyPage data;


    public class IndutyPage implements Serializable{

        private IndutyList list;

        public IndutyList getList() {
            return list;
        }

        public void setList(IndutyList list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "IndutyPage{" +
                    "list=" + list +
                    '}';
        }
    }
    public class IndutyList implements Serializable{
        private int current_page;
        private int last_page;
        private int per_page;
        private int total;
        private List<IndutyData> data;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<IndutyData> getData() {
            return data;
        }

        public void setData(List<IndutyData> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "IndutyList{" +
                    "current_page=" + current_page +
                    ", last_page=" + last_page +
                    ", per_page=" + per_page +
                    ", total=" + total +
                    ", data=" + data +
                    '}';
        }
    }
    public class IndutyData implements Serializable{
        private String id;
        private String img;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "IndutyData{" +
                    "id='" + id + '\'' +
                    ", img='" + img + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public IndutyPage getData() {
        return data;
    }

    public void setData(IndutyPage data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IndutyMustBean{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", data=" + data +
                '}';
    }
}
