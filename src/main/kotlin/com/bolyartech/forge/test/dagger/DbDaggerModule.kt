package com.bolyartech.forge.test.dagger

import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.db.DbPool
import com.bolyartech.forge.server.db.HikariCpDbConfiguration
import com.bolyartech.forge.server.db.HikariCpDbPool
import com.bolyartech.forge.test.data.TestTableDbh
import com.bolyartech.forge.test.data.TestTableDbhImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import javax.sql.DataSource

@Module
class DbDaggerModule(private val dbConfig: HikariCpDbConfiguration) {
    @Provides
    @Singleton
    fun provideComboPooledDataSource(): DataSource {
        return ForgeServer.createDataSourceHelper(dbConfig)
    }

    @Provides
    @Singleton
    internal fun provideDbPool(dbSource: DataSource): DbPool {
        return HikariCpDbPool(dbSource)
    }
}

@Module
abstract class DbDaggerModuleBinds {
    @Binds
    internal abstract fun bindTestTableDbh(impl: TestTableDbhImpl): TestTableDbh
}