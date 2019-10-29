package ru.sigmadigital.taxipro.models;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class DriverDocFile {

    public static class DocFile extends JsonParser {
        int id;
        int type;

        public DocFile(int id, int type) {
            this.id = id;
            this.type = type;
        }
    }

    public static class DocType {
        public static int driverLicense = 0;
        public static int vehicleDocumentTopSide = 1;
        public static int vehicleDocumentBackSide = 2;
        public static int driverLicenseBackSide = 3;

        public static int vehicleFront = 4;
        public static int vehicleBack = 5;
        public static int VehicleInterrior = 6;
    }


    public static class DriverDocFileValue extends JsonParser{
        List<DocFile> files = new ArrayList<>();

        public void addFile(DocFile file) {
            files.add(file);
        }
    }


    public static class UploadDocFiles extends JsonParser {
        int cityId;
        DriverDocFileValue value;

        public UploadDocFiles(int cityId, DriverDocFileValue value) {
            this.cityId = cityId;
            this.value = value;
        }
    }

}
