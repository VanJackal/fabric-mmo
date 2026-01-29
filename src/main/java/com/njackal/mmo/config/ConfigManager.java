package com.njackal.mmo.config;

import com.njackal.mmo.FabricMMO;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

public class ConfigManager {
    private Yaml yaml;
    private MMOConfig config;
    public ConfigManager() {
        yaml = new Yaml();
    }

    /**
     * initialize the config manager from a file, if the file doesn't exist, create it using the default config
     *
     * @param path path to the config file
     * @param defaultPath path to the default config in resource
     */
    public void init(String path, String defaultPath, RegistryAccess registryAccess) throws IOException{
        File file = new File(path);
        if (!file.exists()){
            createConfig(file, defaultPath);
        }

        Registry<Block> blocks = registryAccess.lookupOrThrow(Registries.BLOCK);
        Registry<Item> items = registryAccess.lookupOrThrow(Registries.ITEM);


        InputStream inputStream = new FileInputStream(file);

        Map<String,Map<String,Map<String,Integer>>> map = yaml.load(inputStream);
        config = new MMOConfig(
                BlockBreakConfig.from(map.get("woodcutting"), blocks),
                BlockBreakConfig.from(map.get("mining"), blocks),
                BlockBreakConfig.from(map.get("excavation"), blocks),
                BlockBreakConfig.from(map.get("herbalism"), blocks),
                FishingConfig.from(map.get("fishing"), items),
                map.get("other").get("acrobatics").get("multiplier")
        );

        FabricMMO.LOGGER.info("Config loaded");
        FabricMMO.LOGGER.debug("Config loaded: {}",config);

        inputStream.close();
    }

    private void createConfig(File file, String defaultPath) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(defaultPath)){
            assert inputStream != null;
            Files.copy(inputStream, file.toPath());
        }
    }

    public MMOConfig config(){
        return config;
    }
}
