package com.softyorch.mvvmjetpackcompose

import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionDialTest
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionEmailTest
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionSMSTest
import com.softyorch.mvvmjetpackcompose.data.entity.UserDaoTest
import com.softyorch.mvvmjetpackcompose.integrationTests.DetailsUserCrudIntegrationTest
import com.softyorch.mvvmjetpackcompose.integrationTests.GetUsersIntegrationTest
import com.softyorch.mvvmjetpackcompose.integrationTests.SetUserIntegrationTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    UserDaoTest::class,
    ActionDialTest::class,
    ActionEmailTest::class,
    ActionSMSTest::class,
    GetUsersIntegrationTest::class,
    SetUserIntegrationTest::class,
    DetailsUserCrudIntegrationTest::class
)
class AndroidMainTestSuite