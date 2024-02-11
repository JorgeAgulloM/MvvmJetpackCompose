package com.softyorch.mvvmjetpackcompose

import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionDialTest
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionEmailTest
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionSMSTest
import com.softyorch.mvvmjetpackcompose.data.entity.ContactDaoTest
import com.softyorch.mvvmjetpackcompose.integrationTests.DetailsContactCrudIntegrationTest
import com.softyorch.mvvmjetpackcompose.integrationTests.GetContactsIntegrationTest
import com.softyorch.mvvmjetpackcompose.integrationTests.SetContactIntegrationTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ContactDaoTest::class,
    ActionDialTest::class,
    ActionEmailTest::class,
    ActionSMSTest::class,
    GetContactsIntegrationTest::class,
    SetContactIntegrationTest::class,
    DetailsContactCrudIntegrationTest::class
)
class AndroidMainTestSuite