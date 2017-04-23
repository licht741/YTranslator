package com.licht.ytranslator.utils.ExtendedEditText;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Собственный EditText, который позволяет отслеживать скрывание пользователем клавиатуры,
 * и сообщает об этом подписчику
 */
public class ExtendedEditText extends android.support.v7.widget.AppCompatEditText {

    private ExtendedEditTextListener mOnImeBack;

    public ExtendedEditText(Context context) {
        super(context);
    }

    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        // Проверяем событие закрытия пользователем клавиатуры, и если это оно,
        // то сообщаем об этом листенеру
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null)
                mOnImeBack.onImeBack(this.getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(ExtendedEditTextListener listener) {
        mOnImeBack = listener;
    }

}