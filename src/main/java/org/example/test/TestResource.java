package org.example.test;

import io.quarkus.runtime.annotations.ConfigItem;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.annotations.Property;
import org.jboss.resteasy.reactive.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

@Path("test")
@Slf4j
public class TestResource {

    @Inject
    TestRepository testRepository;

    @ConfigProperty(name = "quarkus.http.body.uploads-directory")
    String uploadedFileLocation;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("database")
    public Uni<List<DatabaseInfo>> hello() {
        return testRepository.getDatabaseInfo();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("file")
    public Response writeFile(@MultipartForm FormData data) throws FileNotFoundException {

        writeToFile(new FileInputStream(data.file.uploadedFile().toAbsolutePath().toString()),uploadedFileLocation+"/"+data.file.fileName());

        String output = "File uploaded to : " + uploadedFileLocation+"/"+ data.file.fileName();

        return Response.status(200).entity(output).build();
    }

    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            new FileOutputStream(uploadedFileLocation);
            OutputStream out;
            int read;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(uploadedFileLocation);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}