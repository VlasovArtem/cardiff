package com.provectus.cardiff.utils.view;

/**
 * Created by artemvlasov on 27/09/15.
 */
public interface CardBookingView {
    interface BasicLevel{}
    interface BookingsLevel extends BasicLevel {}
    interface InfoLevel extends BookingsLevel {}
}
