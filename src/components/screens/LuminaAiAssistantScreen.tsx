import React, { useState, useRef, useEffect } from 'react';
import {
  ArrowLeft,
  Sparkles,
  Send,
  User,
  Bot,
  Compass,
  Zap,
  BookOpen,
  ChevronRight,
  RefreshCw,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const LuminaAiAssistantScreen: React.FC = () => {
  const { aiMessages, sendAiMessage, isAiLoading, navigate, goBack } = useLumina();
  const [inputText, setInputText] = useState('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const suggestionChips = [
    '📖 Summarize current chapter arc',
    '👤 Explain Ichigo hybrid genealogy',
    '🧭 Recommend intense dark battle shonen',
    '⚡ Compare Sukuna vs Aizen power scaling',
  ];

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [aiMessages, isAiLoading]);

  const handleSend = (textToSend?: string) => {
    const text = textToSend || inputText;
    if (!text.trim() || isAiLoading) return;
    sendAiMessage(text.trim());
    setInputText('');
  };

  return (
    <div className="min-h-screen bg-black text-white flex flex-col justify-between">
      {/* Top Header */}
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div className="flex items-center gap-2.5">
            <div className="w-9 h-9 rounded-full bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
              <Sparkles className="w-4 h-4 text-[#00E5FF]" />
            </div>
            <div>
              <h2 className="text-sm font-black text-white flex items-center gap-2">
                Lumina AI
                <span className="text-[10px] bg-[#8B5CF6]/20 text-[#A78BFA] border border-[#8B5CF6]/40 px-1.5 py-0.5 rounded font-mono">
                  Gemini Flash
                </span>
              </h2>
              <p className="text-[11px] text-zinc-400">Narrative co-pilot & manga intelligence</p>
            </div>
          </div>
        </div>
      </header>

      {/* Messages Thread */}
      <main className="flex-1 max-w-xl mx-auto w-full px-4 py-4 space-y-4 overflow-y-auto">
        {aiMessages.map((msg) => (
          <div
            key={msg.id}
            className={`flex gap-3 ${msg.isFromUser ? 'justify-end' : 'justify-start'}`}
          >
            {!msg.isFromUser && (
              <div className="w-8 h-8 rounded-full bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA] flex-shrink-0 mt-0.5">
                <Bot className="w-4 h-4" />
              </div>
            )}

            <div className={`space-y-3 max-w-[85%] ${msg.isFromUser ? 'items-end' : 'items-start'}`}>
              <div
                className={`p-3.5 rounded-2xl text-xs leading-relaxed ${
                  msg.isFromUser
                    ? 'bg-[#8B5CF6] text-white rounded-br-none shadow-md font-medium'
                    : 'bg-[#121216] border border-[#27272A] text-zinc-200 rounded-bl-none shadow-lg'
                }`}
              >
                {msg.text}
              </div>

              {/* Recommendation Cards (if returned by AI) */}
              {msg.recommendations && msg.recommendations.length > 0 && (
                <div className="space-y-2 pt-1 w-full">
                  <span className="text-[10px] font-extrabold uppercase tracking-wider text-[#00E5FF] block">
                    Recommended Series
                  </span>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                    {msg.recommendations.map(rec => (
                      <div
                        key={rec.id}
                        onClick={() => navigate('detail', 1)}
                        className="bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/60 rounded-xl p-2.5 flex gap-2.5 cursor-pointer transition-all group"
                      >
                        <img
                          src={rec.coverUrl}
                          alt={rec.title}
                          className="w-12 h-16 object-cover rounded-lg border border-[#27272A] flex-shrink-0"
                        />
                        <div className="min-w-0 flex-1">
                          <div className="flex items-center justify-between">
                            <h5 className="text-xs font-bold text-white truncate group-hover:text-[#A78BFA]">
                              {rec.title}
                            </h5>
                            <span className="text-[9px] font-mono font-bold text-emerald-400">
                              {rec.matchScore}%
                            </span>
                          </div>
                          <div className="flex gap-1 mt-0.5">
                            <span className="text-[9px] px-1.5 py-0.2 rounded bg-zinc-800 text-zinc-300">
                              {rec.tag1}
                            </span>
                            <span className="text-[9px] px-1.5 py-0.2 rounded bg-zinc-800 text-zinc-300">
                              {rec.tag2}
                            </span>
                          </div>
                          <p className="text-[10px] text-zinc-400 line-clamp-1 mt-1 leading-tight">
                            {rec.description}
                          </p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            {msg.isFromUser && (
              <div className="w-8 h-8 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-zinc-400 flex-shrink-0 mt-0.5">
                <User className="w-4 h-4" />
              </div>
            )}
          </div>
        ))}

        {isAiLoading && (
          <div className="flex gap-3 items-start">
            <div className="w-8 h-8 rounded-full bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA] flex-shrink-0">
              <Sparkles className="w-4 h-4 animate-spin text-[#00E5FF]" />
            </div>
            <div className="bg-[#121216] border border-[#27272A] rounded-2xl rounded-bl-none p-3.5 text-xs text-zinc-400 flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-[#8B5CF6] animate-pulse" />
              Analyzing narrative lore and reading history...
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </main>

      {/* Suggestion Chips & Input Footer */}
      <footer className="sticky bottom-0 bg-black/95 backdrop-blur-md border-t border-[#27272A] p-3 space-y-2 max-w-xl mx-auto w-full">
        {/* Suggestion Chips */}
        <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar pb-1">
          {suggestionChips.map((chip, idx) => (
            <button
              key={idx}
              onClick={() => handleSend(chip)}
              className="px-2.5 py-1 rounded-full bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 text-[11px] font-medium text-zinc-300 whitespace-nowrap transition-colors"
            >
              {chip}
            </button>
          ))}
        </div>

        {/* Input Bar */}
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSend();
          }}
          className="flex items-center gap-2"
        >
          <input
            type="text"
            value={inputText}
            onChange={e => setInputText(e.target.value)}
            placeholder="Ask Lumina AI about manga lore, theories, recs..."
            className="flex-1 bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-4 py-2.5 text-xs text-white placeholder-zinc-500 outline-none transition-colors"
          />
          <button
            type="submit"
            disabled={!inputText.trim() || isAiLoading}
            className="w-10 h-10 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] disabled:opacity-40 text-white flex items-center justify-center shadow-[0_0_12px_rgba(139,92,246,0.4)] transition-all"
          >
            <Send className="w-4 h-4" />
          </button>
        </form>
      </footer>
    </div>
  );
};
