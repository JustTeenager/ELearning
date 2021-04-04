package com.project.eng_assos.dagger.module

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.project.eng_assos.R
import com.project.eng_assos.utils.adapters.BlockOfLevelsAdapter
import com.project.eng_assos.utils.adapters.LevelAdapter
import com.project.eng_assos.utils.adapters.LevelsAdapter
import dagger.Module
import dagger.Provides
import java.lang.NullPointerException

@Module
class BindingHoldersModule(private val inflater: LayoutInflater, private val container: ViewGroup?, private val typeHolder:Int) {

    @Provides
    fun getItemLevelBinding(): ViewDataBinding {
        when(typeHolder){
            LevelsAdapter.TEXT_HOLDER_TYPE ->{
                return DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_text_in_levels,
                    container,
                    false
                )
            }
            LevelsAdapter.LEVELS_HOLDER_TYPE -> {
                return DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_level,
                    container,
                    false
                )
            }
            LevelAdapter.BUTTON_HOLDER_TYPE->{
                return DataBindingUtil.inflate(inflater,R.layout.item_buttons,container,false)
            }
            LevelAdapter.TEXT_LEVEL_HOLDER_TYPE->{
                return DataBindingUtil.inflate(inflater,R.layout.item_text_level,container,false)
            }
            LevelAdapter.LEVEL_HOLDER_TYPE->{
                return DataBindingUtil.inflate(inflater,R.layout.item_word_in_level,container,false)
            }
            BlockOfLevelsAdapter.TITLE_HOLDER_TYPE->{
                return DataBindingUtil.inflate(inflater,R.layout.item_block_levels_title,container,false)
            }
            BlockOfLevelsAdapter.ITEM_HOLDER_TYPE->{
                return DataBindingUtil.inflate(inflater,R.layout.item_block_levels,container,false)
            }
            else -> return throw NullPointerException()
        }
    }
}