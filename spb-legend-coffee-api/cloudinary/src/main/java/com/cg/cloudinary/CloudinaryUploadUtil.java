package com.cg.cloudinary;

import com.cg.exception.DataInputException;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class CloudinaryUploadUtil {

    public final String STAFF_IMAGE_UPLOAD_FOLDER = "staff_images";
    public final String PRODUCT_IMAGE_UPLOAD_FOLDER = "product_images";

    public final String CUSTOMER_IMAGE_UPLOAD_FOLDER = "customer_images";

    public final String ERROR_IMAGE_UPLOAD = "Không thể upload hình ảnh của sản phẩm chưa được lưu.";
    public final String ERROR_IMAGE_DESTROY = "Không thể destroy hình ảnh của sản phẩm không xác định.";

    public Map buildImageUploadParams(String id, String imageFolder, String errorMessage) {
        if (id == null)
            throw new DataInputException(errorMessage);

        String publicId = String.format("%s/%s", imageFolder, id);

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public Map buildImageDestroyParams(String id, String publicId, String errorMessage) {
        if (id == null)
            throw new DataInputException(errorMessage);

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

}

