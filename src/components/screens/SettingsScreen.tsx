import React from 'react';
import {
  ArrowLeft,
  Settings,
  Moon,
  Sparkles,
  Sliders,
  Shield,
  Eye,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { ReaderMode, BackgroundTint } from '../../types';

export const SettingsScreen: React.FC = () => {
  const { readerConfig, updateReaderConfig, goBack, showToast } = useLumina();

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
            <h2 className="text-sm font-black text-white">Reader Settings</h2>
            <p className="text-[11px] text-zinc-400">Display calibration, AMOLED tuning & gestures</p>
          </div>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-5">
        {/* Default Reader Mode */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-3">
          <h3 className="text-xs font-bold text-white flex items-center gap-2">
            <Eye className="w-4 h-4 text-[#8B5CF6]" />
            Default Reading Orientation
          </h3>

          <div className="grid grid-cols-2 gap-2">
            {[
              { mode: ReaderMode.CONTINUOUS_WEBTOON, label: 'Continuous Webtoon' },
              { mode: ReaderMode.SINGLE_PAGE_RTL, label: 'Manga Right-to-Left' },
              { mode: ReaderMode.SINGLE_PAGE_LTR, label: 'Comic Left-to-Right' },
              { mode: ReaderMode.DUAL_PAGE_SPREAD, label: 'Dual Page Spread' },
            ].map(item => (
              <button
                key={item.mode}
                onClick={() => {
                  updateReaderConfig({ readerMode: item.mode });
                  showToast(`Default mode updated to ${item.label}`);
                }}
                className={`p-3 rounded-xl border text-xs font-bold transition-all text-left ${
                  readerConfig.readerMode === item.mode
                    ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white shadow-md'
                    : 'bg-[#18181E] border-[#27272A] text-zinc-400 hover:text-white'
                }`}
              >
                {item.label}
              </button>
            ))}
          </div>
        </div>

        {/* Ambient & Performance */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-3">
          <h3 className="text-xs font-bold text-white flex items-center gap-2">
            <Sparkles className="w-4 h-4 text-[#00E5FF]" />
            Immersion & Display
          </h3>

          <div className="space-y-3">
            <label className="flex items-center justify-between cursor-pointer">
              <div>
                <div className="text-xs font-bold text-white">Lumina Flow Ambient Immersion</div>
                <div className="text-[10px] text-zinc-400">Radiant RGB backglow from active comic panels</div>
              </div>
              <input
                type="checkbox"
                checked={readerConfig.enableLuminaFlow}
                onChange={e => updateReaderConfig({ enableLuminaFlow: e.target.checked })}
                className="w-4 h-4 accent-[#8B5CF6] cursor-pointer"
              />
            </label>

            <label className="flex items-center justify-between cursor-pointer">
              <div>
                <div className="text-xs font-bold text-white">AMOLED True Black Canvas</div>
                <div className="text-[10px] text-zinc-400">Turns off pixels on OLED/AMOLED displays</div>
              </div>
              <input
                type="checkbox"
                checked={readerConfig.backgroundTint === BackgroundTint.PITCH_BLACK}
                onChange={e =>
                  updateReaderConfig({
                    backgroundTint: e.target.checked ? BackgroundTint.PITCH_BLACK : BackgroundTint.CHARCOAL,
                  })
                }
                className="w-4 h-4 accent-[#00E5FF] cursor-pointer"
              />
            </label>

            <label className="flex items-center justify-between cursor-pointer">
              <div>
                <div className="text-xs font-bold text-white">Keep Screen Awake</div>
                <div className="text-[10px] text-zinc-400">Prevent screen lock during long chapter reads</div>
              </div>
              <input
                type="checkbox"
                checked={readerConfig.keepScreenOn}
                onChange={e => updateReaderConfig({ keepScreenOn: e.target.checked })}
                className="w-4 h-4 accent-[#8B5CF6] cursor-pointer"
              />
            </label>
          </div>
        </div>
      </main>
    </div>
  );
};
