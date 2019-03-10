/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.save;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public final class InterfaceAdapterArrayList<T> implements JsonSerializer<ArrayList<T>>, JsonDeserializer<ArrayList<T>> {
    public JsonElement serialize(ArrayList<T> object, Type interfaceType, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (T item : object) {
            final JsonObject wrapper = new JsonObject();
            wrapper.add("data", context.serialize(item));
            wrapper.add("type", context.serialize(item.getClass().getName()));
            array.add(wrapper);
        }
        return array;
    }

    public ArrayList<T> deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        final JsonArray wrapper = (JsonArray) elem;
        ArrayList<T> items = new ArrayList<>();

        for (JsonElement _item: wrapper) {
            JsonObject item = (JsonObject)_item;
            final JsonElement typeName = get(item, "type");
            final JsonElement data = get(item, "data");
            final Type actualType = typeForName(typeName);
            items.add((T)context.deserialize(data, actualType));
        }

        return items;
    }

    private Type typeForName(final JsonElement typeElem) {
        try {
            return Class.forName(typeElem.getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    private JsonElement get(final JsonObject wrapper, String memberName) {
        final JsonElement elem = wrapper.get(memberName);
        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
        return elem;
    }
}