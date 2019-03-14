package task.seedrs

import com.alibaba.fastjson.JSONObject

class Save implements com.arloor.taskmanager.Save {
    @Override
    void save(JSONObject data) {

        String name=data.getString("name")
        String href=data.getString("href")
        String idea=data.getString("idea")
        int page=data.getInteger("page")
        int year=2017-page
        String market=data.getString("market")
        println("save "+href)
        String dirpath="seedrs/"
        File saveDir=new File(dirpath)
        if(!saveDir.exists()){
            saveDir.mkdirs()
        }
        File file=new File(dirpath+href+".md")
        if(!file.exists()){
            file.createNewFile()
        }
        FileWriter writer=new FileWriter(file)
        writer.write("---\n" +
                "title: \""+name+" ("+href+")\"\n" +
                "date: "+year+"-01-01\n" +
                "draft: false\n" +
                "categories: [ \"seedrs\"]\n" +
                "tags: [\"seedrs\"]\n" +
                "weight: 10\n" +
                "---\n\n")
        writer.write("# ["+name+"]("+"https://www.seedrs.com/"+href+")\n\n")
        writer.write(idea)
        if(market!=null){
            writer.write(market)
        }
        writer.close()
    }
}
