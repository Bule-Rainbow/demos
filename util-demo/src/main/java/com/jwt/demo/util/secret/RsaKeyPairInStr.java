package com.jwt.demo.util.secret;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author HuangQi
 * @version 1.0.0
 * @ClassName RsaKeyPairInStr.java
 * @Description rsa公私钥对
 * @createTime 2022年03月14日 11:28
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsaKeyPairInStr {

    private String publicKey;

    private String privateKey;

}
