package com.cg.domain.dto.customerAvatar;

import com.cg.domain.dto.customer.CustomerDTO;
import com.cg.domain.entity.CustomerAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerAvatarDTO {

    private String id;


    private String fileName;


    private String fileFolder;


    private String fileUrl;


    private String fileType;


    private String cloudId;


    private Long ts;


    private CustomerDTO customer;

    public CustomerAvatar toCustomerAvatar() {

        return new CustomerAvatar()
                .setId(id)
                .setFileName(fileName)
                .setFileFolder(fileFolder)
                .setFileUrl(fileUrl)
                .setFileType(fileType)
                .setCloudId(cloudId)
                .setTs(ts)
                .setCustomer(customer.toCustomer());
    }
}
