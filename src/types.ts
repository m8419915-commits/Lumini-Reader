export type ScreenType =
  | 'home'
  | 'library'
  | 'history'
  | 'explore'
  | 'updates'
  | 'more'
  | 'detail'
  | 'reader'
  | 'universe'
  | 'dna'
  | 'forge'
  | 'mutation'
  | 'ai'
  | 'journey'
  | 'timeline'
  | 'backup'
  | 'settings'
  | 'repositories'
  | 'migration';

export interface ScreenState {
  type: ScreenType;
  mangaId?: number;
  chapterId?: number;
  initialPage?: number;
  sourceId?: string;
}

export enum ReaderMode {
  CONTINUOUS_WEBTOON = 'CONTINUOUS_WEBTOON',
  SINGLE_PAGE_LTR = 'SINGLE_PAGE_LTR',
  SINGLE_PAGE_RTL = 'SINGLE_PAGE_RTL',
  DUAL_PAGE_SPREAD = 'DUAL_PAGE_SPREAD',
}

export enum BackgroundTint {
  PITCH_BLACK = 'PITCH_BLACK',
  CHARCOAL = 'CHARCOAL',
  SEPIA = 'SEPIA',
  DEEP_SLATE = 'DEEP_SLATE',
}

export interface ReaderConfig {
  readerMode: ReaderMode;
  backgroundTint: BackgroundTint;
  enableLuminaFlow: boolean;
  keepScreenOn: boolean;
  wifiOnlyDownload: boolean;
  hardwareAcceleration: boolean;
  zoomLevel: number;
  brightness: number;
  cropWhiteBorders?: boolean;
  colorFilter?: 'none' | 'invert' | 'grayscale' | 'high_contrast';
  dualPageSplit?: boolean;
}

export interface Manga {
  id: number;
  title: string;
  author: string;
  artist: string;
  description: string;
  genre: string[];
  status: string;
  thumbnailUrl: string;
  bannerUrl: string;
  inLibrary: boolean;
  category?: string;
  rating: number;
  source: string;
  sourceId?: string;
  totalChapters: number;
  latestChapter: string;
  unreadCount: number;
  lastReadAt?: number;
  trackers?: MangaTracker[];
}

export type TrackerService = 'anilist' | 'myanimelist' | 'kitsu';
export type TrackerStatus = 'READING' | 'COMPLETED' | 'ON_HOLD' | 'DROPPED' | 'PLAN_TO_READ' | 'REREADING';

export interface MangaTracker {
  service: TrackerService;
  serviceName: string;
  icon?: string;
  color: string;
  isConnected: boolean;
  trackingId?: string;
  title: string;
  status: TrackerStatus;
  score: number;
  lastChapterRead: number;
  totalChapters: number;
  startDate?: string;
  finishDate?: string;
}

export interface HistoryItem {
  id: string;
  mangaId: number;
  mangaTitle: string;
  coverUrl: string;
  chapterId: number;
  chapterNumber: number;
  chapterTitle?: string;
  pageIndex: number;
  totalPages: number;
  timestamp?: number;
  readAt?: number;
  progressPercent?: number;
  dateGroup?: string; // 'Today' | 'Yesterday' | '2 days ago' | '3 days ago' | '20/08/26'
  timeString?: string; // '8:43 am'
  isFavorite?: boolean;
}

export type TriState = 'all' | 'included' | 'excluded' | 'none';

export interface LibraryFilters {
  downloaded: TriState;
  unread: TriState;
  completed: TriState;
  started?: TriState;
  tracked?: TriState;
  bookmarked?: TriState;
  category?: string;
  source?: string;
  displayMode?: 'compact_grid' | 'comfortable_grid' | 'list' | 'cover_only';
  sortBy: 'title' | 'lastRead' | 'totalChapters' | 'unread' | 'rating' | 'alphabetical' | 'unreadCount' | 'latestUpdate' | 'dateAdded';
  sortOrder: 'asc' | 'desc';
}

export interface Category {
  id: string;
  name: string;
  order: number;
  count?: number;
  isDefault?: boolean;
}

export interface Chapter {
  id: number;
  mangaId: number;
  chapterNumber: number;
  title: string;
  scanlator: string;
  dateUpload: string;
  isRead: boolean;
  isDownloaded: boolean;
  pageCount: number;
  pages: string[];
}

export interface ReadingSnap {
  mangaId: number;
  title: string;
  coverUrl: string;
  chapterId: number;
  chapterNumber: number;
  pageIndex: number;
  totalPages: number;
  progressPercent: number;
  timestamp: number;
}

export interface UniverseNode {
  id: string;
  name: string;
  role: string;
  powerLevel: string;
  faction: string;
  bio: string;
  color: string;
  x: number;
  y: number;
}

export interface UniverseEdge {
  fromNodeId: string;
  toNodeId: string;
  relationLabel: string;
  color: string;
}

export interface DnaAttribute {
  trait: string;
  intensity: number;
  description: string;
  color: string;
}

export interface Achievement {
  id: string;
  title: string;
  desc: string;
  xp: number;
  unlocked: boolean;
}

export interface ExperiencePack {
  id: string;
  title: string;
  genre: string;
  isActive: boolean;
  isDraft: boolean;
  readingDirection: string;
  hapticIntensity: number;
  backgroundTheme: string;
  audioProfile: string;
  description: string;
}

export interface MutationBehavior {
  id: string;
  title: string;
  type: string;
  description: string;
  isLocked: boolean;
}

export interface AiChatMessage {
  id: string;
  text: string;
  isFromUser: boolean;
  timestamp: number;
  recommendations?: AiRecommendationCard[];
}

export interface AiRecommendationCard {
  id: string;
  title: string;
  coverUrl: string;
  tag1: string;
  tag2: string;
  description: string;
  matchScore: number;
}

export interface MangaUpdateItem {
  id: string;
  mangaId: number;
  title: string;
  chapterDisplay: string;
  coverUrl: string;
  timeAgo: string;
  isDownloaded: boolean;
}

// Mihon & Tachiyomi Extension Architecture Models
export type ExtensionStatus = 'installed' | 'update_available' | 'available' | 'untrusted' | 'obsolete';

export interface SourceMeta {
  id: string;
  name: string;
  lang: string;
  baseUrl: string;
  isPinned: boolean;
  isNsfw: boolean;
  supportsLatest: boolean;
  status: 'online' | 'rate_limited' | 'maintenance' | 'cloudflare';
  icon: string;
  extensionPkg: string;
  itemCount: number;
  version: string;
}

export interface ExtensionPackage {
  name: string;
  packageName: string;
  versionName: string;
  versionCode: number;
  libVersion: string;
  lang: string;
  isNsfw: boolean;
  hasReadme: boolean;
  hasChangelog: boolean;
  apk: string;
  icon: string;
  repoId: string;
  repoName: string;
  status: ExtensionStatus;
  isTrusted: boolean;
  installedVersionName?: string;
  sources: SourceMeta[];
  readmeContent?: string;
  changelogContent?: string;
  installed?: boolean;
}

export interface ExtensionStore {
  id: string;
  name: string;
  baseUrl: string;
  indexUrl: string;
  website: string;
  isOfficial: boolean;
  isPinned: boolean;
  enabled: boolean;
  lastSynced: string;
  totalExtensions: number;
  status: 'synced' | 'syncing' | 'error';
  fingerprint?: string;
}

export interface SourceMigrationItem {
  id: string;
  mangaId: number;
  mangaTitle: string;
  mangaCover: string;
  fromSourceId: string;
  fromSourceName: string;
  toSourceId: string;
  toSourceName: string;
  targetMangaTitle: string;
  targetChapterCount: number;
  status: 'idle' | 'searching' | 'ready' | 'migrated';
  matchScore: number;
}

export interface NetworkSecurityConfig {
  dohProvider: 'default' | 'cloudflare' | 'google' | 'adguard' | 'quad9' | 'disabled';
  customUserAgent: string;
  enableCloudflareResolver: boolean;
  cloudflareBypass?: boolean;
  trustUntrustedExtensions?: boolean;
  rateLimitPerSecond: number;
  requestTimeoutSec: number;
  extensionAutoCheck: boolean;
}

export interface TimelineItem {
  id: string;
  title: string;
  arcBadge: string;
  description: string;
  isMajorEvent: boolean;
  imageUrl?: string;
  dateOrChapter: string;
}

