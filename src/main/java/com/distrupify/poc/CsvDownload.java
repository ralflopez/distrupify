package com.distrupify.poc;

import com.distrupify.validations.EntityFieldsCombinationValidator;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/poc/csv-download")

public class CsvDownload {
    private static final String TEXT_XML = "text/xml";

    private static final Logger LOGGER = Logger.getLogger(CsvDownload.class);

    /**
     * Notes
     * <a href="https://stackoverflow.com/questions/44520887/how-to-download-a-csv-file-by-streamingoutput">Stream PDF</a>
     */
    @GET
    @PermitAll
    public Response downloadCSVFile() {
        StreamingOutput fileStream = output -> {
            try {
                for (int i = 0; i < 60; i++) {
                    final var row = "hi,world\n";
                    byte[] data = row.getBytes();
                    output.write(data);

                    Thread.sleep(1000);
                    LOGGER.info("Added row");
                }

                output.flush();
            } catch (Exception e) {
                throw new WebApplicationException("File Not Found !!");
            }
        };

        return Response
                .ok(fileStream, TEXT_XML)
                .header("content-disposition", "attachment; filename = myfile.csv")
                .build();
    }
}
