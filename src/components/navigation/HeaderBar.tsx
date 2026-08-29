import React from 'react';
import { User, Zap, Search, SlidersHorizontal, ArrowLeft } from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

interface HeaderBarProps {
  title?: string;
  showBack?: boolean;
  onBack?: () => void;
  showRepoManager?: boolean;
  onOpenRepoManager?: () => void;
  rightAction?: React.ReactNode;
}

export const HeaderBar: React.FC<HeaderBarProps> = ({
  title,
  showBack,
  onBack,
  showRepoManager,
  onOpenRepoManager,
  rightAction,
}) => {
  const { snaps, setSnapSwitcherOpen, goBack, canGoBack } = useLumina();

  return (
    <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A]/50 px-4 py-3 flex items-center justify-between">
      <div className="flex items-center gap-3">
        {showBack || canGoBack ? (
          <button
            onClick={onBack || goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
            title="Go Back"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
        ) : (
          <div className="w-9 h-9 rounded-full bg-[#121216] border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
            <User className="w-4 h-4" />
          </div>
        )}

        <div className="flex items-center gap-2">
          <span className="text-lg font-black tracking-wide bg-gradient-to-r from-white via-[#A78BFA] to-[#00E5FF] bg-clip-text text-transparent">
            {title || 'Lumina'}
          </span>
          <span className="text-[10px] font-bold px-1.5 py-0.5 rounded bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 text-[#A78BFA] tracking-wider uppercase">
            AMOLED
          </span>
        </div>
      </div>

      <div className="flex items-center gap-2">
        {/* Quick Lumina Snap 1-sec state restore launcher */}
        <button
          onClick={() => setSnapSwitcherOpen(true)}
          className="relative px-2.5 py-1.5 rounded-xl bg-[#121216] border border-[#27272A] hover:border-[#00E5FF]/50 text-xs font-semibold text-white flex items-center gap-1.5 transition-all hover:shadow-[0_0_12px_rgba(0,229,255,0.2)]"
          title="Lumina Snap: 1-sec Session Restore"
        >
          <Zap className="w-3.5 h-3.5 text-[#00E5FF] fill-[#00E5FF]" />
          <span className="hidden sm:inline text-[#00E5FF]">Snap</span>
          {snaps.length > 0 && (
            <span className="w-4 h-4 rounded-full bg-[#8B5CF6] text-[10px] font-bold text-white flex items-center justify-center">
              {snaps.length}
            </span>
          )}
        </button>

        {showRepoManager && (
          <button
            onClick={onOpenRepoManager}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
            title="Repositories Manager"
          >
            <SlidersHorizontal className="w-4 h-4 text-[#A78BFA]" />
          </button>
        )}

        {rightAction}
      </div>
    </header>
  );
};
