import React, { useEffect, useState } from 'react';
import QRCode from 'react-qr-code';
import { apiService } from '../services/api';
import { toast } from 'react-hot-toast';

const Attendance = () => {
  const [loading, setLoading] = useState(false);
  const [qrToken, setQrToken] = useState(null);
  const [isRegistering, setIsRegistering] = useState(true);

  //Generate random QR token (NOT JWT)
  const generateQrToken = () =>
  'QR-' +
  Date.now().toString(36) +
  '-' +
  Math.random().toString(36).substring(2, 10);

useEffect(() => {
  const registerInitialQr = async () => {
    setIsRegistering(true);
    try {
      const qr = generateQrToken();
      setQrToken(qr);

      await apiService.registerAttendanceToken(qr);
    } catch (err) {
      console.error('QR registration failed:', err);
      toast.error('Failed to register QR');
    } finally {
      setIsRegistering(false);
    }
  };

  registerInitialQr();
}, []);


  //Refresh QR (no JWT, no refreshProfile)
  const handleRefresh = async () => {
    setLoading(true);
    setIsRegistering(true);

    try {
      const newQr = generateQrToken();
      setQrToken(newQr);

      console.log('Registering refreshed QR:', newQr);
      await apiService.registerAttendanceToken(newQr);

      toast.success('QR code refreshed!', {
        duration: 2000,
        position: 'top-center',
      });
    } catch (err) {
      console.error('Refresh failed:', err);
      toast.error(
        err?.response?.data?.message || 'Failed to refresh QR',
        { duration: 2000, position: 'top-center' }
      );
    } finally {
      setLoading(false);
      setIsRegistering(false);
    }
  };

  //Loading state
  if (isRegistering && !qrToken) {
    return (
      <div className="rounded-xl min-h-screen bg-gradient-to-br from-[#3d1f1f] via-[#4a2525] to-[#2d1515] flex items-center justify-center p-3 sm:p-5">
        <div className="bg-gradient-to-br from-amber-50 via-orange-50 to-amber-100 border-2 border-amber-200 rounded-2xl p-5 sm:p-8 shadow-2xl text-center w-full max-w-md">
          <div className="flex flex-col items-center gap-4">
            <div className="animate-spin w-14 h-14 border-4 border-amber-600 border-t-transparent rounded-full"></div>
            <p className="text-amber-900 font-semibold">
              Generating QR Code...
            </p>
          </div>
        </div>
      </div>
    );
  }

  //QR unavailable
  if (!qrToken) {
    return (
      <div className="rounded-xl min-h-screen bg-gradient-to-br from-[#3d1f1f] via-[#4a2525] to-[#2d1515] flex items-center justify-center p-5">
        <div className="bg-amber-100 border-2 border-red-400 rounded-xl p-6 text-center">
          <p className="text-red-700 font-semibold">
            Unable to generate QR code
          </p>
        </div>
      </div>
    );
  }

  //Show QR
  return (
    <div className="rounded-xl min-h-screen bg-gradient-to-br from-[#3d1f1f] via-[#4a2525] to-[#2d1515] flex items-center justify-center p-3 sm:p-5">
      <div className="bg-gradient-to-br from-amber-50 via-orange-50 to-amber-100 border-2 border-amber-200 rounded-2xl p-5 sm:p-8 shadow-2xl w-full max-w-md">
        <div className="flex flex-col items-center space-y-6">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-amber-950">
              Attendance QR Code
            </h2>
            <p className="text-gray-700 text-sm">
              Scan to mark attendance
            </p>
          </div>

          <div className="bg-white p-6 rounded-3xl shadow-xl border-2 border-amber-200">
            {isRegistering ? (
              <div className="animate-pulse text-amber-600">
                Updating...
              </div>
            ) : (
              <QRCode value={qrToken} size={240} />
            )}
          </div>

          <button
            onClick={handleRefresh}
            disabled={loading || isRegistering}
            className="w-full px-6 py-3 bg-gradient-to-r from-orange-600 to-amber-600 disabled:opacity-50 text-white font-semibold rounded-lg shadow-lg"
          >
            {loading ? 'Refreshing...' : 'Refresh QR Code'}
          </button>

          <p className="text-xs text-gray-600">
            QR code is valid for a limited time only
          </p>
        </div>
      </div>
    </div>
  );
};

export default Attendance;
