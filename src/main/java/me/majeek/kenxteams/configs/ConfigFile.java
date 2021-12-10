package me.majeek.kenxteams.configs;

import com.google.common.collect.Lists;
import me.majeek.kenxteams.KenxTeams;
import me.majeek.kenxteams.configs.resolver.Resolver;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ConfigFile {
    private final String fileName;
    private final File file;

    final private YamlConfiguration configuration;
    final private List<Class<?>> annotatedClasses;

    public ConfigFile(String fileName) {
        this.fileName = fileName.replaceAll(".yml", "");
        this.file = new File(KenxTeams.getInstance().getDataFolder(), fileName + ".yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.annotatedClasses = Lists.newArrayList();
    }

    public ConfigFile(String path, String fileName) {
        this.fileName = fileName.replaceAll(".yml", "");
        this.file = new File(new File(KenxTeams.getInstance().getDataFolder(), path), fileName + ".yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.annotatedClasses = Lists.newArrayList();
    }

    public ConfigFile createFile(boolean saveResource) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();

                if (saveResource) {
                    KenxTeams.getInstance().saveResource(file.getName(), true);
                } else {
                    file.createNewFile();
                }
            } catch (IOException exception) {
                KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not create " + this.file.getPath(), exception);
            }
        }

        return this;
    }

    public ConfigFile deleteFile() {
        if(file.exists()) {
            file.delete();
        }

        return this;
    }

    public ConfigFile loadConfig() {
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not load " + this.file.getPath(), exception);
        }

        return this;
    }

    public ConfigFile reloadConfig() {
        loadConfig();
        setAnnotatedFields();
        return this;
    }

    public ConfigFile saveConfig() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not save " + this.file.getPath(), exception);
        }

        return this;
    }

    public ConfigFile registerClass(Class<?> clazz) {
        if (!annotatedClasses.contains(clazz)) {
            annotatedClasses.add(clazz);
        }

        return this;
    }

    public ConfigFile registerClass(Class<?>... classes) {
        return registerClass(Arrays.asList(classes));
    }

    public ConfigFile registerClass(Collection<Class<?>> classes) {
        classes.stream().filter(clazz -> !annotatedClasses.contains(clazz)).forEach(annotatedClasses::add);
        return this;
    }

    public ConfigFile unregisterClass(Class<?> clazz) {
        annotatedClasses.remove(clazz);
        return this;
    }

    public ConfigFile unregisterClass(Class<?>... classes) {
        return unregisterClass(Arrays.asList(classes));
    }

    public ConfigFile unregisterClass(Collection<Class<?>> classes) {
        classes.stream().filter(annotatedClasses::contains).forEach(annotatedClasses::remove);
        return this;
    }

    public ConfigFile export() {
        return export(false);
    }

    public ConfigFile export(boolean overwrite) {
        createFile(false);
        loadConfig();

        annotatedClasses.forEach(clazz -> {
            List<Field> fields = Lists.newArrayList();
            fields.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(declaredField ->
                            declaredField.isAnnotationPresent(ConfigPath.class))
                    .collect(Collectors.toList()));
            addFieldsRecursively(clazz, fields);
            fields.forEach(field -> {
                ConfigPath configPath = field.getAnnotation(ConfigPath.class);

                String path = configPath.path();
                String file = configPath.name();

                if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
                    return;
                }

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                if (!overwrite && configuration.contains(path)) {
                    return;
                }

                try {
                    configuration.set(path, field.get(clazz));
                } catch (IllegalAccessException exception) {
                    KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not export " + this.file.getPath(), exception);
                }
            });
        });

        saveConfig();
        return this;
    }

    public ConfigFile export(Field field) {
        return export(field, true);
    }

    public ConfigFile export(Field field, boolean overwrite) {
        createFile(false);
        loadConfig();

        ConfigPath configPath = field.getAnnotation(ConfigPath.class);

        String path = configPath.path();
        String file = configPath.name();

        if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
            return this;
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        if (!overwrite && configuration.contains(path)) {
            return this;
        }

        try {
            configuration.set(path, field.get(field.getDeclaringClass()));
        } catch (IllegalAccessException exception) {
            KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not export " + this.file.getPath(), exception);
        }

        saveConfig();
        return this;
    }

    public ConfigFile setAnnotatedFields() {
        annotatedClasses.forEach(clazz -> {
            if (!annotatedClasses.contains(clazz)) {
                annotatedClasses.add(clazz);
            }

            List<Field> fields = Lists.newArrayList();
            fields.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(declaredField ->
                            declaredField.isAnnotationPresent(ConfigPath.class))
                    .collect(Collectors.toList()));
            addFieldsRecursively(clazz, fields);
            fields.forEach(field -> {
                ConfigPath configPath = field.getAnnotation(ConfigPath.class);

                String path = configPath.path();
                String file = configPath.name();

                if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
                    return;
                }

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                Resolver<?> resolver = ConfigHelper.getResolverMap().get(field.getType());
                if (resolver == null) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    resolver = ConfigHelper.getResolverTable().get(field.getType(), parameterizedType.getActualTypeArguments()[0]);

                    if (resolver == null) {
                        return;
                    }
                }

                Object value = configuration.get(path);
                if (value == null) {
                    return;
                }

                try {
                    field.set(clazz, value);
                } catch (IllegalAccessException exception) {
                    KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not set annotations at " + this.file.getPath(), exception);
                }
            });
        });

        return this;
    }

    public ConfigFile setAnnotatedField(Field field) {
        return setAnnotatedField(field, true);
    }

    public ConfigFile setAnnotatedField(Field field, boolean overwrite) {
        ConfigPath configPath = field.getAnnotation(ConfigPath.class);

        String path = configPath.path();
        String file = configPath.name();

        if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
            return this;
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            if (overwrite && field.get(field.getDeclaringClass()) == null) {
                return this;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Resolver<?> resolver = ConfigHelper.getResolverMap().get(field.getType());
        if (resolver == null) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            resolver = ConfigHelper.getResolverTable().get(field.getType(), parameterizedType.getActualTypeArguments()[0]);

            if (resolver == null) {
                return this;
            }
        }

        Object value = configuration.get(path);
        if (value == null) {
            return this;
        }

        try {
            field.set(field.getDeclaringClass(), value);
        } catch (IllegalAccessException exception) {
            KenxTeams.getInstance().getLogger().log(Level.SEVERE, "Could not set annotation field at " + this.file.getPath(), exception);
        }

        return this;
    }

    public void addFieldsRecursively(Class<?> clazz, Collection<Field> fields) {
        Arrays.stream(clazz.getDeclaredClasses()).forEach(declaredClass -> {
            addFieldsRecursively(declaredClass, fields);
            fields.addAll(Arrays.stream(declaredClass.getDeclaredFields()).filter(declaredField ->
                            declaredField.isAnnotationPresent(ConfigPath.class))
                    .collect(Collectors.toList())
            );
        });
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
