/*
 * Designed and developed by 2024 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.getstream.ai.chat.core.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import javax.inject.Inject

class AIChatNavigator @Inject constructor() : AppComposeNavigator<AIChatScreen>() {

  override fun navigate(
    route: AIChatScreen,
    optionsBuilder: (NavOptionsBuilder.() -> Unit)?
  ) {
    val options = optionsBuilder?.let { navOptions(it) }
    navigationCommands.tryEmit(ComposeNavigationCommand.NavigateToRoute(route, options))
  }

  override fun navigateAndClearBackStack(route: AIChatScreen) {
    navigationCommands.tryEmit(
      ComposeNavigationCommand.NavigateToRoute(
        route,
        navOptions {
          popUpTo(0)
        }
      )
    )
  }

  override fun popUpTo(route: AIChatScreen, inclusive: Boolean) {
    navigationCommands.tryEmit(ComposeNavigationCommand.PopUpToRoute(route, inclusive))
  }

  override fun <R> navigateBackWithResult(key: String, result: R, route: AIChatScreen?) {
    navigationCommands.tryEmit(
      ComposeNavigationCommand.NavigateUpWithResult(
        key = key,
        result = result,
        route = route
      )
    )
  }
}