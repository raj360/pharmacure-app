package com.pharmacure.online_pharmacy_app.viewmodels.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pharmacure.online_pharmacy_app.PharmacureApplication
import org.kodein.di.*
import org.kodein.type.TypeToken
import org.kodein.type.erased


/**
 * Gets a factory of `T` for the given argument type, return type and tag.
 *
 * @param A The type of argument the returned factory takes.
 * @param T The type of object to retrieve with the returned factory.
 * @param argType The type of argument the returned factory takes.
 * @param type The type of object to retrieve with the returned factory.
 * @param tag The bound tag, if any.
 * @return A factory of `T`.
 * @throws DI.NotFoundException If no factory was found.
 * @throws DI.DependencyLoopException When calling the factory, if the value construction triggered a dependency loop.
 */

class ViewModelFactory(appContext: Context) : ViewModelProvider.Factory, DIAware {
    override val di: DI = (appContext as PharmacureApplication).di

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        di.direct.Instance(erased(modelClass))

}





