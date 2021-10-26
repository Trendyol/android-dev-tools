package com.trendyol.devtools.autofill.internal.domain

import com.trendyol.devtools.autofill.internal.model.ListItemEntity
import com.trendyol.devtools.autofill.internal.model.ListItem

internal class ItemEntityMapper {

    fun mapFromEntity(entity: ListItemEntity): ListItem.Autofill {
        return ListItem.Autofill(
            name = entity.values.first(),
            data = entity.values,
        )
    }

    fun mapToEntity(fields: List<String>, item: ListItem.Autofill): ListItemEntity {
        return ListItemEntity(
            fields = fields,
            values = item.data,
        )
    }
}
