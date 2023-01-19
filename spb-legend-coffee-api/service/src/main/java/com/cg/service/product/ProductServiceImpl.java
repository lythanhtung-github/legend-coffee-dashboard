package com.cg.service.product;

import com.cg.cloudinary.CloudinaryUploadUtil;
import com.cg.domain.dto.product.ProductCashierDTO;
import com.cg.domain.dto.product.ProductCreateDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.ProductImage;
import com.cg.domain.enums.EnumFileType;
import com.cg.exception.DataInputException;
import com.cg.repository.ProductRepository;
import com.cg.service.category.ICategoryService;
import com.cg.service.productImage.IProductImageService;
import com.cg.service.upload.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class ProductServiceImpl implements IProductService {
   private final String DEFAULT_PRODUCT_IMAGE_ID = "default-product-image";
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductImageService productImageService;

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private CloudinaryUploadUtil cloudinaryUploadUtil;

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public List<ProductDTO> getAllProductDTOWhereDeletedIsFalse() {
        return productRepository.getAllProductDTOWhereDeletedIsFalse();
    }

    @Override
    public List<ProductCashierDTO> getAllProductCashierDTOWhereDeletedIsFalse() {
        return productRepository.getAllProductCashierDTOWhereDeletedIsFalse();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean existsByTitle(String title) {
        return productRepository.existsByTitle(title);
    }

    @Override
    public boolean existsByProductNameAndIdNot(String title, Long id) {
        return productRepository.existsByTitleAndIdNot(title, id);
    }

    @Override
    public Product createWithImage(ProductCreateDTO productCreateDTO, MultipartFile file) {

        Category category = categoryService.findById(Long.valueOf(productCreateDTO.getCategoryId())).get();

        String fileType = file.getContentType();
        assert fileType != null;
        fileType = fileType.substring(0, 5);
        ProductImage productImage = new ProductImage();
        productImage.setFileType(fileType);
        productImage = productImageService.save(productImage);

        if (fileType.equals(EnumFileType.IMAGE.getValue())) {
            productImage = uploadAndSaveProductImage(file, productImage);
        }

        Product product = productCreateDTO.toProduct(category, productImage);

        product = productRepository.save(product);

        return product;
    }

    @Override
    public Product createNoImage(ProductCreateDTO productCreateDTO) {
        Category category = categoryService.findById(Long.valueOf(productCreateDTO.getCategoryId())).get();


        ProductImage productImage = productImageService.findById(DEFAULT_PRODUCT_IMAGE_ID).get();

        productImage = productImageService.save(productImage);

        Product product = productCreateDTO.toProduct(category, productImage);

        product = productRepository.save(product);

        return product;

    }

    @Override
    public Product saveWithImage(Product product, MultipartFile file) {

            String fileType = file.getContentType();
            assert fileType != null;
            fileType = fileType.substring(0, 5);

            ProductImage productImage = new ProductImage();

            productImage.setFileType(fileType);
            productImage = productImageService.save(productImage);

            if (fileType.equals(EnumFileType.IMAGE.getValue())) {
                productImage = uploadAndSaveProductImage(file, productImage);
            }

            product.setProductImage(productImage);
            product = productRepository.save(product);

            return product;

    }

    @Override
    public ProductImage uploadAndSaveProductImage(MultipartFile file, ProductImage productImage) {
        try {
            Map uploadResult = uploadService.uploadImage(
                    file,
                    cloudinaryUploadUtil.buildImageUploadParams(
                        productImage.getId(),
                        cloudinaryUploadUtil.PRODUCT_IMAGE_UPLOAD_FOLDER,
                        cloudinaryUploadUtil.ERROR_IMAGE_UPLOAD
                    )
            );
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");
            int height = (int) uploadResult.get("height");
            int width = (int) uploadResult.get("width");

            productImage.setWidth(width);
            productImage.setHeight(height);
            productImage.setFileName(productImage.getId() + "." + fileFormat);
            productImage.setFileUrl(fileUrl);
            productImage.setFileFolder(cloudinaryUploadUtil.PRODUCT_IMAGE_UPLOAD_FOLDER);
            productImage.setCloudId(productImage.getFileFolder() + "/" + productImage.getId());

            return productImageService.save(productImage);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại.");
        }
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void softDelete(Long productId) {
        productRepository.softDelete(productId);
    }
}
