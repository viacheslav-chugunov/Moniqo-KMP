package io.github.viacheslav.chugunov.moniqo.test.mock.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.storage.db.RatePairEntity
import io.github.viacheslav.chugunov.moniqo.storage.mapper.RatePairStorageMapper
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairMock

class RatePairStorageMapperMock(
    private val result: RatePair = ratePairMock,
) : RatePairStorageMapper {
    var lastEntity: RatePairEntity? = null

    override fun toDomain(entity: RatePairEntity): RatePair {
        lastEntity = entity
        return result
    }
}
