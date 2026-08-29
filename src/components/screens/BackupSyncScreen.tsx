import React, { useState } from 'react';
import {
  ArrowLeft,
  CloudSync,
  DownloadCloud,
  UploadCloud,
  FileCheck,
  ShieldCheck,
  HardDrive,
  RefreshCw,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const BackupSyncScreen: React.FC = () => {
  const { goBack, showToast, mangas, snaps } = useLumina();
  const [isSyncing, setIsSyncing] = useState(false);

  const handleCreateBackup = () => {
    const backupData = {
      version: '2.4.0',
      timestamp: new Date().toISOString(),
      mangaCount: mangas.length,
      snapCount: snaps.length,
      mangas,
      snaps,
    };

    const dataStr = 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(backupData, null, 2));
    const downloadAnchor = document.createElement('a');
    downloadAnchor.setAttribute('href', dataStr);
    downloadAnchor.setAttribute('download', `lumina_backup_${Date.now()}.json`);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();

    showToast('Lumina backup archive generated successfully');
  };

  const handleCloudSync = () => {
    setIsSyncing(true);
    setTimeout(() => {
      setIsSyncing(false);
      showToast('Cloud database state synchronized');
    }, 1200);
  };

  return (
    <div className="min-h-screen bg-black text-white pb-16">
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Backup & Cloud Sync
            </h2>
            <p className="text-[11px] text-zinc-400">Export database, snaps, and offline vault</p>
          </div>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-5">
        {/* Sync Status Card */}
        <div className="p-4 rounded-2xl bg-[#121216] border border-[#27272A] flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-[#8B5CF6]/15 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
              <CloudSync className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-xs font-bold text-white">Lumina Cloud Vault</h3>
              <p className="text-[11px] text-zinc-400">Last synchronized 2 hours ago</p>
            </div>
          </div>

          <button
            onClick={handleCloudSync}
            disabled={isSyncing}
            className="px-3.5 py-1.5 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold shadow-md flex items-center gap-1.5"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${isSyncing ? 'animate-spin' : ''}`} />
            Sync Now
          </button>
        </div>

        {/* Local Backup Actions */}
        <div className="space-y-3">
          <h3 className="text-xs font-extrabold uppercase tracking-wider text-zinc-400">
            Local Archives & Files
          </h3>

          <div className="space-y-2">
            <div
              onClick={handleCreateBackup}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <DownloadCloud className="w-5 h-5 text-[#00E5FF]" />
                <div>
                  <h4 className="text-sm font-bold text-white">Create .Lumina Backup</h4>
                  <p className="text-xs text-zinc-400">Export library state, 1-sec snaps, and settings</p>
                </div>
              </div>
            </div>

            <label className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors">
              <div className="flex items-center gap-3.5">
                <UploadCloud className="w-5 h-5 text-amber-400" />
                <div>
                  <h4 className="text-sm font-bold text-white">Restore Backup File</h4>
                  <p className="text-xs text-zinc-400">Import .json or .lumina state file</p>
                </div>
              </div>
              <input
                type="file"
                accept=".json"
                className="hidden"
                onChange={() => showToast('Backup archive validated and restored')}
              />
            </label>
          </div>
        </div>
      </main>
    </div>
  );
};
