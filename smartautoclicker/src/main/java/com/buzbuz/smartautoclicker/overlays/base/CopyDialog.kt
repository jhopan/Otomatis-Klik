/*
 * Copyright (C) 2022 Nain57
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */
package com.buzbuz.smartautoclicker.overlays.base

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView

import androidx.appcompat.widget.SearchView

import com.buzbuz.smartautoclicker.R
import com.buzbuz.smartautoclicker.baseui.dialog.OverlayDialogController
import com.buzbuz.smartautoclicker.databinding.DialogCopyBinding
import com.buzbuz.smartautoclicker.overlays.bindings.setEmptyText
import com.buzbuz.smartautoclicker.overlays.utils.setIconTint

import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class CopyDialog(
    context: Context,
) : OverlayDialogController(context) {

    /** ViewBinding containing the views for this dialog. */
    protected lateinit var viewBinding: DialogCopyBinding

    final override fun onCreateDialog(): BottomSheetDialog {
        viewBinding = DialogCopyBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.setText(R.string.dialog_copy_title)
                buttonDismiss.setOnClickListener { dismiss() }

                search.apply {
                    findViewById<ImageView>(androidx.appcompat.R.id.search_button).setIconTint(R.color.overlayViewPrimary)
                    findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setIconTint(R.color.overlayViewPrimary)

                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?) = false
                        override fun onQueryTextChange(newText: String?): Boolean {
                            onSearchQueryChanged(newText)
                            return true
                        }
                    })
                }
            }

            layoutLoadableList.setEmptyText(R.string.dialog_copy_empty)
        }

        return BottomSheetDialog(context).apply {
            setContentView(viewBinding.root)
        }
    }

    abstract fun onSearchQueryChanged(newText: String?)
}
