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
package io.getstream.ai.chat.feature.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.ai.chat.core.data.repository.ChannelsRepository
import io.getstream.ai.chat.core.model.Channel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ChannelsViewModel @Inject constructor(
  private val channelsRepository: ChannelsRepository
) : ViewModel() {

  val channels: StateFlow<List<Channel>> = channelsRepository.fetchChannels()
    .flatMapLatest { result ->
      flowOf(result.getOrNull())
    }
    .filterNotNull()
    .map { snapshot -> snapshot.channels }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  fun handleEvents(channelsEvent: ChannelsEvent) {
    when (channelsEvent) {
      is ChannelsEvent.CreateChannel -> channelsRepository.addChannel(channels.value)
    }
  }
}

sealed interface ChannelsEvent {

  data object CreateChannel : ChannelsEvent
}
