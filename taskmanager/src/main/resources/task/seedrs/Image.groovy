package task.seedrs

import com.arloor.taskmanager.Step
import com.arloor.taskmanager.StepContext
import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest

class Image implements Step{
    @Override
    HttpUriRequest createRequest(StepContext context) {
        return new HttpGet(context.url)
    }

    @Override
    void doParse(CloseableHttpResponse response, StepContext context) {
        String path=new URI(context.url).path

        String saveDirPath="seedrs"+path.substring(0,path.lastIndexOf("/"))
        File saveDir=new File(saveDirPath)
        if(!saveDir.exists()){
            saveDir.mkdirs()
        }
        File img=new File("seedrs"+path)
        if(!img.exists()){
            img.createNewFile()
        }
        FileOutputStream outputStream=new FileOutputStream(img);
        HttpEntity entity= response.getEntity()
        entity.writeTo(outputStream)
        outputStream.close()
    }
}
