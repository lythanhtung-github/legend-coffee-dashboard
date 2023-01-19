package com.cg.domain.dto.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SizeDTODeserializer extends StdDeserializer<List<SizeDTO>> {
    public SizeDTODeserializer() {
        this(SizeDTO.class);
    }
    public SizeDTODeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<SizeDTO> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<SizeDTO> sizes = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            JsonNode nodeSize= iterator.next().getValue();
            String name = nodeSize.get("name").asText();
            String price = nodeSize.get("price").asText();
            sizes.add(new SizeDTO(name, price));
        }
        return sizes;
    }
}
