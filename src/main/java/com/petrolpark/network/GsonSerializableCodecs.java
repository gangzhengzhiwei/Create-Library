package com.petrolpark.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.petrolpark.Petrolpark;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.Deserializers;

public class GsonSerializableCodecs {

    public static final Codec<NumberProvider> NUMBER_PROVIDER = GSONserializableCodec("number provider", NumberProvider.class, Deserializers.createConditionSerializer().create());

    /**
     * Construct a Codec for a class that has a defined JSON {@link Serializer}.
     * @param name Name of class to show in error messages
     * @param jsonSerializableObjectClass
     * @param gson Must be capable of (de)serializing objects of the given class.
     * @see Deserializers Examples of such GSONs
     */
    public static <GSON_SERIALIZABLE_OBJECT> Codec<GSON_SERIALIZABLE_OBJECT> GSONserializableCodec(String name, Class<GSON_SERIALIZABLE_OBJECT> jsonSerializableObjectClass, Gson gson) {
        gson.getAdapter(jsonSerializableObjectClass); // Just in case, throw error at this stage
        return Codec.PASSTHROUGH.flatXmap(
            d -> {
                try {
                    GSON_SERIALIZABLE_OBJECT object = gson.fromJson(getJson(d), jsonSerializableObjectClass);
                    return DataResult.success(object);
                } catch (JsonSyntaxException e) {
                    Petrolpark.LOGGER.warn("Unable to decode "+name, e);
                    return DataResult.error(e::getMessage);
                }
            },
            object -> {
                try {
                    JsonElement element = gson.toJsonTree(object);
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
                } catch (JsonSyntaxException e) {
                    Petrolpark.LOGGER.warn("Unable to encode "+name, e);
                    return DataResult.error(e::getMessage);
                }
            }
        );
    };

    @SuppressWarnings("unchecked")
    protected static <U> JsonElement getJson(Dynamic<?> dynamic) {
        Dynamic<U> typed = (Dynamic<U>) dynamic;
        return typed.getValue() instanceof JsonElement jsonElement ? jsonElement : typed.getOps().convertTo(JsonOps.INSTANCE, typed.getValue());
    };
    
};
