package com.paymentreminder.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PaymentDao_Impl implements PaymentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PaymentEntity> __insertionAdapterOfPaymentEntity;

  private final EntityDeletionOrUpdateAdapter<PaymentEntity> __deletionAdapterOfPaymentEntity;

  private final EntityDeletionOrUpdateAdapter<PaymentEntity> __updateAdapterOfPaymentEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsPaid;

  public PaymentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPaymentEntity = new EntityInsertionAdapter<PaymentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `payments` (`id`,`vendorName`,`invoiceNumber`,`dueDateEpoch`,`amount`,`isPaid`,`paidDateEpoch`,`isMonthlyReminder`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getVendorName());
        statement.bindString(3, entity.getInvoiceNumber());
        statement.bindLong(4, entity.getDueDateEpoch());
        statement.bindDouble(5, entity.getAmount());
        final int _tmp = entity.isPaid() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getPaidDateEpoch() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPaidDateEpoch());
        }
        final int _tmp_1 = entity.isMonthlyReminder() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfPaymentEntity = new EntityDeletionOrUpdateAdapter<PaymentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `payments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPaymentEntity = new EntityDeletionOrUpdateAdapter<PaymentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `payments` SET `id` = ?,`vendorName` = ?,`invoiceNumber` = ?,`dueDateEpoch` = ?,`amount` = ?,`isPaid` = ?,`paidDateEpoch` = ?,`isMonthlyReminder` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getVendorName());
        statement.bindString(3, entity.getInvoiceNumber());
        statement.bindLong(4, entity.getDueDateEpoch());
        statement.bindDouble(5, entity.getAmount());
        final int _tmp = entity.isPaid() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getPaidDateEpoch() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPaidDateEpoch());
        }
        final int _tmp_1 = entity.isMonthlyReminder() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfMarkAsPaid = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE payments SET isPaid = 1, paidDateEpoch = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPayment(final PaymentEntity payment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPaymentEntity.insertAndReturnId(payment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePayment(final PaymentEntity payment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPaymentEntity.handle(payment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePayment(final PaymentEntity payment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPaymentEntity.handle(payment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsPaid(final long id, final long paidEpoch, final long now,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsPaid.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, paidEpoch);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, now);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfMarkAsPaid.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PaymentEntity>> getAllPayments() {
    final String _sql = "SELECT * FROM payments ORDER BY dueDateEpoch ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
          final int _cursorIndexOfDueDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpoch");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final int _cursorIndexOfPaidDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDateEpoch");
          final int _cursorIndexOfIsMonthlyReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "isMonthlyReminder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpInvoiceNumber;
            _tmpInvoiceNumber = _cursor.getString(_cursorIndexOfInvoiceNumber);
            final long _tmpDueDateEpoch;
            _tmpDueDateEpoch = _cursor.getLong(_cursorIndexOfDueDateEpoch);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidDateEpoch;
            if (_cursor.isNull(_cursorIndexOfPaidDateEpoch)) {
              _tmpPaidDateEpoch = null;
            } else {
              _tmpPaidDateEpoch = _cursor.getLong(_cursorIndexOfPaidDateEpoch);
            }
            final boolean _tmpIsMonthlyReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsMonthlyReminder);
            _tmpIsMonthlyReminder = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new PaymentEntity(_tmpId,_tmpVendorName,_tmpInvoiceNumber,_tmpDueDateEpoch,_tmpAmount,_tmpIsPaid,_tmpPaidDateEpoch,_tmpIsMonthlyReminder,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOverduePayments(final long todayEpoch,
      final Continuation<? super List<PaymentEntity>> $completion) {
    final String _sql = "SELECT * FROM payments WHERE isPaid = 0 AND dueDateEpoch < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayEpoch);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
          final int _cursorIndexOfDueDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpoch");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final int _cursorIndexOfPaidDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDateEpoch");
          final int _cursorIndexOfIsMonthlyReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "isMonthlyReminder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpInvoiceNumber;
            _tmpInvoiceNumber = _cursor.getString(_cursorIndexOfInvoiceNumber);
            final long _tmpDueDateEpoch;
            _tmpDueDateEpoch = _cursor.getLong(_cursorIndexOfDueDateEpoch);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidDateEpoch;
            if (_cursor.isNull(_cursorIndexOfPaidDateEpoch)) {
              _tmpPaidDateEpoch = null;
            } else {
              _tmpPaidDateEpoch = _cursor.getLong(_cursorIndexOfPaidDateEpoch);
            }
            final boolean _tmpIsMonthlyReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsMonthlyReminder);
            _tmpIsMonthlyReminder = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new PaymentEntity(_tmpId,_tmpVendorName,_tmpInvoiceNumber,_tmpDueDateEpoch,_tmpAmount,_tmpIsPaid,_tmpPaidDateEpoch,_tmpIsMonthlyReminder,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getDueSoonPayments(final long todayEpoch, final long endEpoch,
      final Continuation<? super List<PaymentEntity>> $completion) {
    final String _sql = "SELECT * FROM payments WHERE isPaid = 0 AND dueDateEpoch BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayEpoch);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endEpoch);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
          final int _cursorIndexOfDueDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpoch");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final int _cursorIndexOfPaidDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDateEpoch");
          final int _cursorIndexOfIsMonthlyReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "isMonthlyReminder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpInvoiceNumber;
            _tmpInvoiceNumber = _cursor.getString(_cursorIndexOfInvoiceNumber);
            final long _tmpDueDateEpoch;
            _tmpDueDateEpoch = _cursor.getLong(_cursorIndexOfDueDateEpoch);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidDateEpoch;
            if (_cursor.isNull(_cursorIndexOfPaidDateEpoch)) {
              _tmpPaidDateEpoch = null;
            } else {
              _tmpPaidDateEpoch = _cursor.getLong(_cursorIndexOfPaidDateEpoch);
            }
            final boolean _tmpIsMonthlyReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsMonthlyReminder);
            _tmpIsMonthlyReminder = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new PaymentEntity(_tmpId,_tmpVendorName,_tmpInvoiceNumber,_tmpDueDateEpoch,_tmpAmount,_tmpIsPaid,_tmpPaidDateEpoch,_tmpIsMonthlyReminder,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getPaymentById(final long id,
      final Continuation<? super PaymentEntity> $completion) {
    final String _sql = "SELECT * FROM payments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PaymentEntity>() {
      @Override
      @Nullable
      public PaymentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
          final int _cursorIndexOfDueDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpoch");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final int _cursorIndexOfPaidDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDateEpoch");
          final int _cursorIndexOfIsMonthlyReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "isMonthlyReminder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final PaymentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpInvoiceNumber;
            _tmpInvoiceNumber = _cursor.getString(_cursorIndexOfInvoiceNumber);
            final long _tmpDueDateEpoch;
            _tmpDueDateEpoch = _cursor.getLong(_cursorIndexOfDueDateEpoch);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidDateEpoch;
            if (_cursor.isNull(_cursorIndexOfPaidDateEpoch)) {
              _tmpPaidDateEpoch = null;
            } else {
              _tmpPaidDateEpoch = _cursor.getLong(_cursorIndexOfPaidDateEpoch);
            }
            final boolean _tmpIsMonthlyReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsMonthlyReminder);
            _tmpIsMonthlyReminder = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new PaymentEntity(_tmpId,_tmpVendorName,_tmpInvoiceNumber,_tmpDueDateEpoch,_tmpAmount,_tmpIsPaid,_tmpPaidDateEpoch,_tmpIsMonthlyReminder,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Flow<Integer> getTotalCount() {
    final String _sql = "SELECT COUNT(*) FROM payments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getOverdueCount(final long todayEpoch) {
    final String _sql = "SELECT COUNT(*) FROM payments WHERE isPaid = 0 AND dueDateEpoch < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayEpoch);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getDueSoonCount(final long todayEpoch, final long endEpoch) {
    final String _sql = "SELECT COUNT(*) FROM payments WHERE isPaid = 0 AND dueDateEpoch BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayEpoch);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endEpoch);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getPaidCount() {
    final String _sql = "SELECT COUNT(*) FROM payments WHERE isPaid = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getTotalUnpaidAmount() {
    final String _sql = "SELECT COALESCE(SUM(amount), 0.0) FROM payments WHERE isPaid = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findByVendorAndDate(final String vendorName, final long dueDateEpoch,
      final Continuation<? super PaymentEntity> $completion) {
    final String _sql = "SELECT * FROM payments WHERE vendorName = ? AND dueDateEpoch = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, vendorName);
    _argIndex = 2;
    _statement.bindLong(_argIndex, dueDateEpoch);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PaymentEntity>() {
      @Override
      @Nullable
      public PaymentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
          final int _cursorIndexOfDueDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpoch");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final int _cursorIndexOfPaidDateEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDateEpoch");
          final int _cursorIndexOfIsMonthlyReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "isMonthlyReminder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final PaymentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpInvoiceNumber;
            _tmpInvoiceNumber = _cursor.getString(_cursorIndexOfInvoiceNumber);
            final long _tmpDueDateEpoch;
            _tmpDueDateEpoch = _cursor.getLong(_cursorIndexOfDueDateEpoch);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidDateEpoch;
            if (_cursor.isNull(_cursorIndexOfPaidDateEpoch)) {
              _tmpPaidDateEpoch = null;
            } else {
              _tmpPaidDateEpoch = _cursor.getLong(_cursorIndexOfPaidDateEpoch);
            }
            final boolean _tmpIsMonthlyReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsMonthlyReminder);
            _tmpIsMonthlyReminder = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new PaymentEntity(_tmpId,_tmpVendorName,_tmpInvoiceNumber,_tmpDueDateEpoch,_tmpAmount,_tmpIsPaid,_tmpPaidDateEpoch,_tmpIsMonthlyReminder,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
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
