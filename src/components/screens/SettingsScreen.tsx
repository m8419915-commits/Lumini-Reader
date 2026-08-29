import React from 'react';
import {
  ArrowLeft,
  Settings,
  Moon,
  Sparkles,
  Sliders,
  Shield,
  Eye,
  Layers,
  Globe,
  Shuffle,
  ChevronRight,
  Wifi,
  Lock,
  DownloadCloud,
  CheckCircle2
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { ReaderMode, BackgroundTint } from '../../types';

export const SettingsScreen: React.FC = () => {
  const {
    readerConfig,
    updateReaderConfig,
    networkConfig,
    updateNetworkConfig,
    goBack,
    navigate,
    showToast
  } = useLumina();

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            id="settings-back-btn"
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white">System Settings</h2>
            <p className="text-[11px] text-zinc-400">Repositories, DoH network security & reader calibration</p>
          </div>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-5">
        {/* Mihon Extensions & Repositories Hub */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-3">
          <h3 className="text-xs font-bold text-white flex items-center gap-2">
            <Layers className="w-4 h-4 text-[#8B5CF6]" />
            Extensions & Repositories
          </h3>

          <div className="space-y-2">
            <div
              id="goto-repo-manager-settings"
              onClick={() => navigate('repositories')}
              className="bg-black/60 hover:bg-black/80 border border-[#27272A] hover:border-[#8B5CF6]/60 rounded-xl p-3 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3">
                <Globe className="w-4 h-4 text-[#00E5FF]" />
                <div>
                  <h4 className="text-xs font-bold text-white">Repository Manager</h4>
                  <p className="text-[10px] text-zinc-400">Configure Keiyoushi, Komikku & custom index.min.json</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>

            <div
              id="goto-migration-settings"
              onClick={() => navigate('migration')}
              className="bg-black/60 hover:bg-black/80 border border-[#27272A] hover:border-[#8B5CF6]/60 rounded-xl p-3 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3">
                <Shuffle className="w-4 h-4 text-[#A78BFA]" />
                <div>
                  <h4 className="text-xs font-bold text-white">Source Migration Hub</h4>
                  <p className="text-[10px] text-zinc-400">Transfer reading state when scanlation sources change</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>
          </div>
        </div>

        {/* Network & DNS Security (Mihon OkHttp Engine) */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-4">
          <h3 className="text-xs font-bold text-white flex items-center gap-2">
            <Shield className="w-4 h-4 text-emerald-400" />
            Network & DNS-over-HTTPS (DoH)
          </h3>

          <div className="space-y-3 text-xs">
            <div className="space-y-1.5">
              <label className="text-[11px] font-semibold text-zinc-400">Secure DNS Provider</label>
              <select
                value={networkConfig.dohProvider}
                onChange={e => {
                  updateNetworkConfig({ dohProvider: e.target.value as any });
                  showToast(`DNS provider set to ${e.target.value}`);
                }}
                className="w-full bg-black border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2 text-xs text-white outline-none"
              >
                <option value="cloudflare">Cloudflare (1.1.1.1) - Recommended</option>
                <option value="google">Google (8.8.8.8)</option>
                <option value="quad9">Quad9 (9.9.9.9)</option>
                <option value="adguard">AdGuard DNS (Anti-Tracking)</option>
                <option value="disabled">System Default (No DoH)</option>
              </select>
            </div>

            <label className="flex items-center justify-between cursor-pointer pt-1">
              <div>
                <div className="text-xs font-bold text-white">Cloudflare Turnstile & FlareSolverr Proxy</div>
                <div className="text-[10px] text-zinc-400">Automate challenge clearance for protected scanlation endpoints</div>
              </div>
              <input
                type="checkbox"
                checked={networkConfig.cloudflareBypass}
                onChange={e => updateNetworkConfig({ cloudflareBypass: e.target.checked })}
                className="w-4 h-4 accent-[#00E5FF] cursor-pointer"
              />
            </label>

            <label className="flex items-center justify-between cursor-pointer">
              <div>
                <div className="text-xs font-bold text-white">Trust Unofficial Extensions</div>
                <div className="text-[10px] text-zinc-400">Allow installation from external non-signed repositories</div>
              </div>
              <input
                type="checkbox"
                checked={networkConfig.trustUntrustedExtensions}
                onChange={e => updateNetworkConfig({ trustUntrustedExtensions: e.target.checked })}
                className="w-4 h-4 accent-[#8B5CF6] cursor-pointer"
              />
            </label>
          </div>
        </div>

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

