package com.xmx.kotlinmapbase.common.data.sync

import com.xmx.kotlinmapbase.common.data.cloud.ICloudEntity
import com.xmx.kotlinmapbase.common.data.sql.ISQLEntity

/**
 * Created by The_onE on 2016/5/29.
 * SQLite数据库与LeanCloud数据库同步实体，实现该接口后即可通过管理器管理
 */
interface ISyncEntity : ICloudEntity, ISQLEntity {
    // 云端中的objectId，保存至本地数据库用于同步
    var cloudId: String
}
