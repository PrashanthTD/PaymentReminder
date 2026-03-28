package com.paymentreminder.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PaymentNotificationDao_Impl implements PaymentNotificationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PaymentNotificationEntity> __insertionAdapterOfPaymentNotificationEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForPayment;

  public PaymentNotificationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPaymentNotificationEntity = new EntityInsertionAdapter<PaymentNotificationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `payment_notifications` (`id`,`paymentId`,`scheduledAt`,`isCustom`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentNotificationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPaymentId());
        statement.bindLong(3, entity.getScheduledAt());
        final int _tmp = entity.isCustom() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__preparedStmtOfDeleteForPayment = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM payment_notifications WHERE paymentId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<PaymentNotificationEntity> notifications,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfPaymentNotificationEntity.insertAndReturnIdsList(notifications);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteForPayment(final long paymentId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForPayment.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, paymentId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteForPayment.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getForPayment(final long paymentId,
      final Continuation<? super List<PaymentNotificationEntity>> $completion) {
    final String _sql = "SELECT * FROM payment_notifications WHERE paymentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, paymentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentNotificationEntity>>() {
      @Override
      @NonNull
      public List<PaymentNotificationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentId");
          final int _cursorIndexOfScheduledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledAt");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
          final List<PaymentNotificationEntity> _result = new ArrayList<PaymentNotificationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentNotificationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPaymentId;
            _tmpPaymentId = _cursor.getLong(_cursorIndexOfPaymentId);
            final long _tmpScheduledAt;
            _tmpScheduledAt = _cursor.getLong(_cursorIndexOfScheduledAt);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            _item = new PaymentNotificationEntity(_tmpId,_tmpPaymentId,_tmpScheduledAt,_tmpIsCustom);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCustomForPayment(final long paymentId,
      final Continuation<? super List<PaymentNotificationEntity>> $completion) {
    final String _sql = "SELECT * FROM payment_notifications WHERE paymentId = ? AND isCustom = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, paymentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentNotificationEntity>>() {
      @Override
      @NonNull
      public List<PaymentNotificationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentId");
          final int _cursorIndexOfScheduledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledAt");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
          final List<PaymentNotificationEntity> _result = new ArrayList<PaymentNotificationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentNotificationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPaymentId;
            _tmpPaymentId = _cursor.getLong(_cursorIndexOfPaymentId);
            final long _tmpScheduledAt;
            _tmpScheduledAt = _cursor.getLong(_cursorIndexOfScheduledAt);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            _item = new PaymentNotificationEntity(_tmpId,_tmpPaymentId,_tmpScheduledAt,_tmpIsCustom);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getFutureNotifications(final long nowMillis,
      final Continuation<? super List<PaymentNotificationEntity>> $completion) {
    final String _sql = "SELECT * FROM payment_notifications WHERE scheduledAt > ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, nowMillis);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentNotificationEntity>>() {
      @Override
      @NonNull
      public List<PaymentNotificationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentId");
          final int _cursorIndexOfScheduledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledAt");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
          final List<PaymentNotificationEntity> _result = new ArrayList<PaymentNotificationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentNotificationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPaymentId;
            _tmpPaymentId = _cursor.getLong(_cursorIndexOfPaymentId);
            final long _tmpScheduledAt;
            _tmpScheduledAt = _cursor.getLong(_cursorIndexOfScheduledAt);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            _item = new PaymentNotificationEntity(_tmpId,_tmpPaymentId,_tmpScheduledAt,_tmpIsCustom);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
